package com.winfooz.validation.rules

import android.widget.EditText
import com.winfooz.validation.ConfirmValidationRule
import com.winfooz.validation.annotations.ConfirmEmail

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class ConfirmEmailValidationRule : ConfirmValidationRule<EditText, CharSequence, ConfirmEmail> {

    override fun isValid(view: EditText?, data: CharSequence?, annotation: ConfirmEmail?): Boolean {
        return view?.text?.toString() == data?.toString()
    }
}
