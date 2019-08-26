package com.winfooz.annotations

import com.winfooz.AnnotationsErrorHandlingAdapter
import kotlin.reflect.KClass

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorHandling(val value: KClass<out AnnotationsErrorHandlingAdapter>)
