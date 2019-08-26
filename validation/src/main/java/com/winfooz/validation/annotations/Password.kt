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
import com.winfooz.validation.rules.PasswordValidationRule

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Adapter(EditTextAdapter::class)
@AutoValidateWith(EditTextAutoValidate::class)
@Rule(PasswordValidationRule::class)
@ErrorHandling(EditTextErrorHandling::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Password(
    @IdRes val value: Int,
    @StringRes val messageResId: Int,
    val scheme: Scheme = Scheme.ANY,
    val min: Int = 6
) {

    enum class Scheme {

        ANY,
        ALPHA,
        ALPHA_MIXED_CASE,
        NUMERIC,
        ALPHA_NUMERIC,
        ALPHA_NUMERIC_MIXED_CASE,
        ALPHA_NUMERIC_SYMBOLS,
        ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,
        ALPHA_NUMERIC_ALLOW_SYMBOLS_WITHOUT_SPACE
    }
}
