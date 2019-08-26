package com.winfooz.validation.rules

import com.winfooz.validation.ValidationRule
import com.winfooz.validation.annotations.Digits

/**
 * Project: WinValidation
 * Created: Aug 11, 2019
 *
 * @author Mohamed Hamdan
 */
class DigitsValidationRule : ValidationRule<CharSequence, Digits> {

    override fun isValid(data: CharSequence?, annotation: Digits?): Boolean {
        return data?.matches(DIGITS_REGEX.toRegex()) == true
    }

    private companion object {

        private const val DIGITS_REGEX = "[0-9]+"
    }
}