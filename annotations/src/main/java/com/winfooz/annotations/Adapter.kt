package com.winfooz.annotations

import com.winfooz.AnnotationsValidateAdapter
import kotlin.reflect.KClass

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Adapter(val value: KClass<out AnnotationsValidateAdapter>)
