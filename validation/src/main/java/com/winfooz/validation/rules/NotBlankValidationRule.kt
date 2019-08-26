package com.winfooz.validation.rules

import com.winfooz.validation.ValidationRule
import com.winfooz.validation.annotations.NotBlank

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class NotBlankValidationRule : ValidationRule<CharSequence, NotBlank> {

    override fun isValid(data: CharSequence?, annotation: NotBlank?): Boolean {
        return data != null && data.isNotBlank()
    }
}
