package com.winfooz.validation.annotations

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.winfooz.annotations.*
import com.winfooz.validation.adapters.EditTextAdapter
import com.winfooz.validation.adapters.autovalidate.EditTextAutoValidate
import com.winfooz.validation.errorhandlingadapters.EditTextErrorHandling
import com.winfooz.validation.rules.ConfirmEmailValidationRule

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Adapter(EditTextAdapter::class)
@AutoValidateWith(EditTextAutoValidate::class)
@ConfirmWith(Email::class)
@Rule(ConfirmEmailValidationRule::class)
@ErrorHandling(EditTextErrorHandling::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ConfirmEmail(@IdRes val value: Int, @StringRes val messageResId: Int)
