package com.winfooz.validation.rules

import com.winfooz.validation.ValidationRule
import com.winfooz.validation.annotations.Email

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class EmailValidationRule : ValidationRule<CharSequence, Email> {

    override fun isValid(data: CharSequence?, annotation: Email?): Boolean {
        if (data == null) {
            return false
        }
        return Regex(EMAIL_REGEX).matches(data)
    }

    private companion object {

        private const val EMAIL_REGEX = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]" +
                "{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    }
}
