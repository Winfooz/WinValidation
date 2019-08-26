package com.winfooz.validation.rules

import com.winfooz.validation.ValidationRule
import com.winfooz.validation.annotations.Password
import com.winfooz.validation.annotations.Password.Scheme


/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class PasswordValidationRule : ValidationRule<CharSequence, Password> {

    override fun isValid(data: CharSequence?, annotation: Password?): Boolean {
        if (data == null || annotation == null) {
            return false
        }
        val hasMinChars = data.length >= annotation.min
        val matchesScheme = SCHEME_PATTERNS[annotation.scheme]?.toRegex()?.matches(data) == true
        return hasMinChars && matchesScheme
    }

    private companion object {

        private val SCHEME_PATTERNS = mapOf(
            Scheme.ANY to ".+",
            Scheme.ALPHA to "\\w+",
            Scheme.ALPHA_MIXED_CASE to "(?=.*[a-z])(?=.*[A-Z]).+",
            Scheme.NUMERIC to "\\d+",
            Scheme.ALPHA_NUMERIC to "(?=.*[a-zA-Z])(?=.*[\\d]).+",
            Scheme.ALPHA_NUMERIC_MIXED_CASE to "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).+",
            Scheme.ALPHA_NUMERIC_SYMBOLS to "(?=.*[a-zA-Z])(?=.*[\\d])(?=.*([^\\w]|_)).+",
            Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS to "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*([^\\w]|_)).+",
            Scheme.ALPHA_NUMERIC_ALLOW_SYMBOLS_WITHOUT_SPACE to "^(?=.*[0-9])(?=.*[A-Za-z!@#$%^&*()_+])(?=\\S+$).+$"
        )
    }
}
