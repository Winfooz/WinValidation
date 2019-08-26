package com.winfooz.validation.annotations

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.winfooz.annotations.Adapter
import com.winfooz.annotations.AutoValidateWith
import com.winfooz.annotations.ErrorHandling
import com.winfooz.annotations.Rule
import com.winfooz.validation.adapters.CheckBoxAdapter
import com.winfooz.validation.adapters.autovalidate.CheckBoxAutoValidate
import com.winfooz.validation.errorhandlingadapters.CheckBoxErrorHandling
import com.winfooz.validation.rules.CheckBoxValidationRule

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Adapter(CheckBoxAdapter::class)
@AutoValidateWith(CheckBoxAutoValidate::class)
@Rule(CheckBoxValidationRule::class)
@ErrorHandling(CheckBoxErrorHandling::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Checked(@IdRes val value: Int, @StringRes val messageResId: Int)
