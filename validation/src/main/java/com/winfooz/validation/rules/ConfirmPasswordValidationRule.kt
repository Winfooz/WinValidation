package com.winfooz.validation.rules

import android.widget.EditText
import com.winfooz.validation.ConfirmValidationRule
import com.winfooz.validation.annotations.ConfirmPassword

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class ConfirmPasswordValidationRule : ConfirmValidationRule<EditText, CharSequence, ConfirmPassword> {

    override fun isValid(view: EditText?, data: CharSequence?, annotation: ConfirmPassword?): Boolean {
        return view?.text?.toString() == data?.toString()
    }
}
