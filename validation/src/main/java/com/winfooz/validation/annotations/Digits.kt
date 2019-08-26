package com.winfooz.validation.annotations

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.winfooz.annotations.Adapter
import com.winfooz.annotations.AutoValidateWith
import com.winfooz.annotations.ErrorHandling
import com.winfooz.annotations.Rule
import com.winfooz.validation.adapters.EditTextAdapter
import com.winfooz.validation.adapters.autovalidate.EditTextAutoValidate
import com.winfooz.validation.errorhandlingadapters.EditTextErrorHandling
import com.winfooz.validation.rules.DigitsValidationRule

/**
 * Project: WinValidation
 * Created: Aug 11, 2019
 *
 * @author Mohamed Hamdan
 */
@Adapter(EditTextAdapter::class)
@AutoValidateWith(EditTextAutoValidate::class)
@Rule(DigitsValidationRule::class)
@ErrorHandling(EditTextErrorHandling::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Digits(@IdRes val value: Int, @StringRes val messageResId: Int)
