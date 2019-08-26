package com.winfooz.validation.rules

import com.winfooz.validation.ValidationRule
import com.winfooz.validation.annotations.NotEmpty

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class NotEmptyValidationRule : ValidationRule<CharSequence, NotEmpty> {

    override fun isValid(data: CharSequence?, annotation: NotEmpty?): Boolean {
        return data != null && data.isNotEmpty()
    }
}
