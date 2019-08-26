package com.winfooz.annotations

import androidx.annotation.IdRes

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidateOnClick(@IdRes val value: Int)
