package com.winfooz.validation.rules

import com.winfooz.validation.ValidationRule
import com.winfooz.validation.annotations.Checked

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class CheckBoxValidationRule : ValidationRule<Boolean, Checked> {

    override fun isValid(data: Boolean?, annotation: Checked?): Boolean {
        return data == true
    }
}
