package com.winfooz.annotations

import com.winfooz.AnnotationsValidationRule
import kotlin.reflect.KClass

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Rule(val value: KClass<out AnnotationsValidationRule>)
