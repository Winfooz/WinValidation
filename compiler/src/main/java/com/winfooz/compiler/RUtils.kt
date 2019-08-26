package com.winfooz.compiler

import com.sun.source.util.Trees
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeScanner
import java.util.*
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class RUtils(trees: Trees) {

    private val rScanner = RScanner()
    private var trees: Trees? = trees

    fun elementToId(element: Element, annotation: Class<*>, value: Int?): String {
        val tree = trees?.getTree(element, getMirror(element, annotation)) as? JCTree?
        rScanner.reset()
        tree?.accept(rScanner)
        if (rScanner.resourceIds.isNotEmpty()) {
            return getId(rScanner.resourceIds.iterator().next())
        }

        return getId(value to null)
    }

    fun elementToStringId(element: Element, annotation: Class<*>, value: Int?): String {
        val tree = trees?.getTree(element, getMirror(element, annotation)) as? JCTree?
        rScanner.reset()
        tree?.accept(rScanner)
        if (rScanner.resourceIds.isNotEmpty()) {
            val iterator = rScanner.resourceIds.iterator()
            iterator.next()
            return getId(iterator.next())
        }

        return getId(value to null)
    }

    private fun getMirror(element: Element, annotation: Class<*>): AnnotationMirror? {
        for (annotationMirror in element.annotationMirrors) {
            if (annotationMirror.annotationType.toString() == annotation.canonicalName) {
                return annotationMirror
            }
        }
        return null
    }

    private fun getId(pair: Pair<Int?, Symbol?>): String {
        val (value, rSymbol) = pair
        return if (rSymbol != null) {
            "${rSymbol.packge().qualifiedName}.R.${rSymbol.enclClass().name}.${rSymbol.name}"
        } else {
            "$value"
        }
    }

    private class RScanner : TreeScanner() {

        var resourceIds: MutableList<Pair<Int, Symbol?>> = mutableListOf()

        override fun visitSelect(jcFieldAccess: JCTree.JCFieldAccess) {
            val symbol = jcFieldAccess.sym
            if (symbol.enclosingElement != null && symbol.enclosingElement.enclosingElement != null &&
                symbol.enclosingElement.enclosingElement.enclClass() != null
            ) {
                try {
                    val value = Objects.requireNonNull((symbol as Symbol.VarSymbol).constantValue) as Int
                    resourceIds.add(value to symbol)
                } catch (ignored: Exception) {
                }
            }
        }

        override fun visitLiteral(jcLiteral: JCTree.JCLiteral) {
            try {
                val value = jcLiteral.value as Int
                resourceIds.add(value to null)
            } catch (ignored: Exception) {
            }

        }

        fun reset() {
            resourceIds.clear()
        }
    }
}
