package com.winfooz.annotations

import com.winfooz.AnnotationsAutoValidate
import kotlin.reflect.KClass

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoValidateWith(val value: KClass<out AnnotationsAutoValidate>)
