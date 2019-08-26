package com.winfooz.compiler

import com.squareup.kotlinpoet.*
import com.sun.source.util.Trees
import com.winfooz.annotations.*
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ValidationProcessor : AbstractProcessor() {

    private val addedTypes: MutableList<String> = mutableListOf()
    private val elements: MutableMap<TypeElement, MutableList<out Element>> = mutableMapOf()
    private val elementUtils: Elements by lazy { processingEnv.elementUtils }
    private val messager: Messager by lazy { processingEnv.messager }
    private var rUtils: RUtils? = null

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
        try {
            rUtils = RUtils(Trees.instance(processingEnv))
        } catch (ignored: IllegalArgumentException) {
        }
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Validations::class.java.canonicalName)
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(Validations::class.java).forEach {
            elements[it as TypeElement] = ElementFilter.fieldsIn(elementUtils.getAllMembers(it))
        }

        startProcessingElements()
        return true
    }

    private fun startProcessingElements() {
        elements.forEach { entry ->
            addedTypes.clear()

            val typeElement = entry.key
            val elements = entry.value
            val packageName = (typeElement.enclosingElement as PackageElement).qualifiedName.toString()
            val validationTypeSpec = TypeSpec.classBuilder("${typeElement.simpleName}_Validation")
            val constructorFunSpec = FunSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder("target", entry.key.asClassName()).build())
                .addParameter(ParameterSpec.builder("source", VIEW_CLASS_NAME).build())
            val validateFunSpec = FunSpec.builder("validate").addModifiers(KModifier.PRIVATE)
                .returns(Boolean::class.java)
                .addParameter(ParameterSpec.builder("context", CONTEXT_CLASS_NAME).build())
                .addParameter(ParameterSpec.builder("target", typeElement.asClassName()).build())
                .addCode(CodeBlock.of("var isValid = true\n"))
            val autoValidateFunSpec = FunSpec.builder("handleAutoValidate").addModifiers(KModifier.PRIVATE)
                .addParameter(ParameterSpec.builder("context", CONTEXT_CLASS_NAME).build())
                .addParameter(ParameterSpec.builder("target", typeElement.asClassName()).build())


            handleOnClick(constructorFunSpec, entry.key)
            elements.forEach { element ->
                val annotations = element.annotationMirrors
                val validationAnnotationMirror = getValidationAnnotation(annotations)
                val validationAnnotation = validationAnnotationMirror?.annotationType
                if (validationAnnotation != null) {
                    val autoValidate = element.getAnnotation(AutoValidate::class.java)
                    val rule = getRule(validationAnnotation)
                    if (rule != null) {
                        val errorHandling = getErrorHandling(validationAnnotation)
                        val adapter = getAdapter(validationAnnotation)
                        val confirmWith = getConfirmWith(validationAnnotation)
                        val confirmWithElement = findAndValidateConfirmWith(elements, confirmWith)

                        if (autoValidate != null) {
                            startValidateAutoValidateWith(validationAnnotation, element)
                            val autoValidateWith = getAutoValidateWith(validationAnnotation)
                            autoValidateData(
                                element,
                                autoValidateFunSpec,
                                autoValidateWith
                            )
                        }

                        addTypeProperty(validationTypeSpec, rule)
                        addTypeProperty(validationTypeSpec, errorHandling)
                        addTypeProperty(validationTypeSpec, adapter)
                        findView(element, validationAnnotationMirror, constructorFunSpec, validationTypeSpec)
                        validateData(
                            validationAnnotation,
                            element,
                            validationTypeSpec,
                            validateFunSpec,
                            rule,
                            errorHandling,
                            confirmWithElement,
                            adapter,
                            validationAnnotationMirror,
                            typeElement
                        )
                    }
                }
            }

            try {
                constructorFunSpec.addCode(CodeBlock.of("handleAutoValidate(source.context, target)\n"))
                validateFunSpec.addCode(CodeBlock.of("return isValid\n"))
                validationTypeSpec.addFunction(constructorFunSpec.build())
                    .addFunction(validateFunSpec.build())
                    .addFunction(autoValidateFunSpec.build())
                val javaFile = FileSpec.builder(packageName, "${typeElement.simpleName}_Validation")
                    .addType(validationTypeSpec.build())
                    .build()
                javaFile.writeTo(processingEnv.filer)
            } catch (ignored: IOException) {
            }
        }
    }

    private fun getValidationAnnotation(annotations: List<AnnotationMirror>): AnnotationMirror? {
        return annotations.mapNotNull { if (it.annotationType.asElement().getAnnotation(Rule::class.java) != null) it else null }
            .firstOrNull()
    }

    private fun autoValidateData(
        element: Element,
        autoValidateFunSpec: FunSpec.Builder,
        autoValidateWith: TypeMirror?
    ) {
        autoValidateFunSpec.beginControlFlow(
            "val %N_%N = %T() {",
            element.simpleName.toString(),
            autoValidateWith.toString().replace(".", "_"),
            autoValidateWith!!.asTypeName()
        )
        autoValidateFunSpec.addCode("validate%N(context, target)\n", element.simpleName.toString().capitalize())
        autoValidateFunSpec.endControlFlow()
        autoValidateFunSpec.addCode(
            CodeBlock.of(
                "%N_%N.start(%N)\n",
                element.simpleName.toString(),
                autoValidateWith.toString().replace(".", "_"),
                element.simpleName.toString()
            )
        )
    }

    private fun handleOnClick(constructorFunSpec: FunSpec.Builder, type: TypeElement) {
        val element =
            elementUtils.getAllMembers(type).firstOrNull { it.getAnnotation(ValidateOnClick::class.java) != null }
        val validateOnClick = element?.getAnnotation(ValidateOnClick::class.java)
        constructorFunSpec.addCode(
            CodeBlock.of(
                "source.findViewById<View>(%L)",
                rUtils?.elementToId(element!!, ValidateOnClick::class.java, validateOnClick?.value)
                    ?: validateOnClick?.value
            )
        )
        constructorFunSpec.beginControlFlow(".setOnClickListener")
        constructorFunSpec.addCode("val isValid = %N(%N.context, target)\n", "validate", "source")
        if ((element as ExecutableElement).parameters.size == 0) {
            constructorFunSpec.beginControlFlow("if(isValid)")
            constructorFunSpec.addCode(CodeBlock.of("target.%N()\n", element.simpleName.toString()))
            constructorFunSpec.endControlFlow()
        } else {
            val paramType = (element.parameters).first().asType()
            val isPrimitiveBoolean = paramType.toString() == "boolean"
            val isBoolean = paramType.toString() == "java.lang.Boolean"

            if (isPrimitiveBoolean || isBoolean) {
                constructorFunSpec.addCode(CodeBlock.of("target.%N(isValid)\n", element.simpleName.toString()))
            } else {
                messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "The method which annotated with @ValidateOnClick should have zero or one Boolean parameter",
                    element,
                    element.annotationMirrors.first()
                )
            }
        }
        constructorFunSpec.endControlFlow()
    }

    private fun getRule(declaredType: DeclaredType): TypeMirror? {
        val rule = declaredType.asElement().getAnnotation(Rule::class.java)
        return if (rule != null) {
            return try {
                elementUtils.getTypeElement(rule.value.java.canonicalName).asType()
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
        } else {
            null
        }
    }

    private fun getErrorHandling(declaredType: DeclaredType): TypeMirror? {
        val errorHandling = declaredType.asElement().getAnnotation(ErrorHandling::class.java)
        return if (errorHandling != null) {
            return try {
                elementUtils.getTypeElement(errorHandling.value.java.canonicalName).asType()
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
        } else {
            null
        }
    }

    private fun getAdapter(declaredType: DeclaredType): TypeMirror? {
        val adapter = declaredType.asElement().getAnnotation(Adapter::class.java)
        return if (adapter != null) {
            return try {
                elementUtils.getTypeElement(adapter.value.java.canonicalName).asType()
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
        } else {
            null
        }
    }

    private fun getConfirmWith(declaredType: DeclaredType): TypeMirror? {
        val rule = declaredType.asElement().getAnnotation(ConfirmWith::class.java)
        return if (rule != null) {
            return try {
                elementUtils.getTypeElement(rule.value.java.canonicalName).asType()
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
        } else {
            null
        }
    }

    private fun findAndValidateConfirmWith(elements: MutableList<out Element>, confirmWith: TypeMirror?): Element? {
        if (confirmWith == null) {
            return null
        }
        var confirmWithElement: Element? = null
        elements.forEach { element ->
            val validateUsing = element.annotationMirrors.map { it.annotationType }

            if (validateUsing.any { it.toString() == confirmWith.toString() }) {
                confirmWithElement = element
            }
        }
        if (confirmWithElement == null) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Cannot find any field annotated with @ValidateUsing($confirmWith::class)"
            )
        }
        return confirmWithElement
    }

    private fun startValidateAutoValidateWith(declaredType: DeclaredType?, element: Element) {
        val autoValidateWith = declaredType?.asElement()?.getAnnotation(AutoValidateWith::class.java)
        if (autoValidateWith == null) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "$declaredType should be annotated with @AutoValidateWith if you are using @AutoValidate",
                element,
                element.annotationMirrors.first()
            )
        }
    }

    private fun getAutoValidateWith(declaredType: DeclaredType?): TypeMirror? {
        val autoValidateWith = declaredType?.asElement()?.getAnnotation(AutoValidateWith::class.java)
        return if (autoValidateWith != null) {
            return try {
                elementUtils.getTypeElement(autoValidateWith.value.java.canonicalName).asType()
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
        } else {
            null
        }
    }

    private fun addTypeProperty(validationTypeSpec: TypeSpec.Builder, type: TypeMirror?) {
        val ruleName = type.toString().replace(".", "_")
        if (!addedTypes.contains(ruleName)) {
            val ruleProperty = PropertySpec.builder(ruleName, type!!.asTypeName(), KModifier.PRIVATE)
                .delegate(CodeBlock.of("lazy { %T() }", type.asTypeName()))
                .build()
            validationTypeSpec.addProperty(ruleProperty)
        }
        addedTypes.add(ruleName)
    }

    private fun findView(
        element: Element,
        declaredType: AnnotationMirror?,
        constructor: FunSpec.Builder,
        type: TypeSpec.Builder
    ) {
        type.addProperty(
            PropertySpec.builder(
                element.simpleName.toString(),
                element.asType().asTypeName().copy(nullable = true),
                KModifier.PRIVATE
            ).build()
        )

        val idElement = declaredType?.elementValues?.keys?.firstOrNull { it.simpleName.toString() == "value" }
        val idValue =
            declaredType?.elementValues?.filter { it.key.simpleName.toString() == "value" }?.values?.firstOrNull()
        if (idElement?.returnType?.kind != TypeKind.INT) {
            messager.printMessage(Diagnostic.Kind.ERROR, "The value should be int", element, declaredType)
        }
        constructor.addCode(
            CodeBlock.of(
                "target.%N = source.findViewById(%L)\n",
                element.simpleName,
                rUtils?.elementToId(
                    element,
                    Class.forName(declaredType?.annotationType.toString()),
                    (idValue?.value as Int)
                ) ?: (idValue?.value as Int)
            )
        )

        constructor.addCode(
            CodeBlock.of(
                "this.%N = target.%N\n",
                element.simpleName,
                element.simpleName
            )
        )
    }

    private fun validateData(
        declaredType: DeclaredType,
        element: Element,
        validationTypeSpec: TypeSpec.Builder,
        validateFunSpec: FunSpec.Builder,
        rule: TypeMirror?,
        errorHandling: TypeMirror?,
        confirmWithElement: Element?,
        adapter: TypeMirror?,
        annotationMirror: AnnotationMirror?,
        typeElement: TypeElement
    ) {
        val messageResIdElement =
            annotationMirror?.elementValues?.keys?.firstOrNull { it.simpleName.toString() == "messageResId" }
        val messageResIdValue =
            annotationMirror?.elementValues?.filter { it.key.simpleName.toString() == "value" }?.values?.firstOrNull()
        if (messageResIdElement?.returnType?.kind != TypeKind.INT) {
            messager.printMessage(Diagnostic.Kind.ERROR, "The value should be int", element, annotationMirror)
        }

        val elementValidationFunSpec = FunSpec.builder("validate${element.simpleName.toString().capitalize()}")
            .returns(Boolean::class)
            .addParameter(ParameterSpec.builder("context", CONTEXT_CLASS_NAME).build())
            .addParameter(ParameterSpec.builder("target", typeElement.asClassName()).build())
        elementValidationFunSpec.addCode(
            CodeBlock.of(
                "val %N = %N.getData(%N)\n",
                "${element.simpleName}Data",
                adapter.toString().replace(".", "_"),
                element.simpleName.toString()
            )
        )

        elementValidationFunSpec.addCode(
            CodeBlock.of(
                "val annotation = %N.javaClass.getDeclaredField(%S).getAnnotation(%T::class.java)\n",
                "target",
                element.simpleName.toString(),
                declaredType
            )
        )

        if (confirmWithElement == null) {
            elementValidationFunSpec.beginControlFlow(
                "if (!%N.isValid(%N, annotation))",
                rule.toString().replace(".", "_"),
                "${element.simpleName}Data"
            )
        } else {
            elementValidationFunSpec.beginControlFlow(
                "if (!%N.isValid(%N, %N, annotation))",
                rule.toString().replace(".", "_"),
                confirmWithElement.simpleName.toString(),
                "${element.simpleName}Data"
            )
        }
        elementValidationFunSpec.addCode(
            CodeBlock.of(
                "%N.handleError(%N, context.getString(%L), annotation)\n",
                errorHandling.toString().replace(".", "_"),
                element.simpleName.toString(),
                rUtils?.elementToStringId(
                    element,
                    Class.forName(annotationMirror?.annotationType.toString()),
                    messageResIdValue?.value as Int
                ) ?: messageResIdValue?.value as Int
            )
        )
        elementValidationFunSpec.addCode(CodeBlock.of("return false\n"))
        elementValidationFunSpec.nextControlFlow("else")
        elementValidationFunSpec.addCode(
            CodeBlock.of(
                "%N.handleError(%N, null, annotation)\n",
                errorHandling.toString().replace(".", "_"),
                element.simpleName.toString()
            )
        )
        elementValidationFunSpec.addCode(CodeBlock.of("return true\n"))
        elementValidationFunSpec.endControlFlow()

        validationTypeSpec.addFunction(elementValidationFunSpec.build())

        validateFunSpec.beginControlFlow(
            "if (!validate${element.simpleName.toString().capitalize()}(%N, %N))",
            "context",
            "target"
        )
        validateFunSpec.addCode(CodeBlock.of("isValid = false\n"))
        validateFunSpec.endControlFlow()
    }
}
