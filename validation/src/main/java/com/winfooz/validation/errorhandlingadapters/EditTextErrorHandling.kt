package com.winfooz.validation.errorhandlingadapters

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.winfooz.validation.ErrorHandling
import com.winfooz.validation.R

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class EditTextErrorHandling : ErrorHandling<EditText, Annotation> {

    override fun handleError(view: EditText?, message: String?, annotation: Annotation?) {
        val parent = view?.parent?.parent
        if (parent is TextInputLayout) {
            parent.isErrorEnabled = message != null
            parent.error = message
            parent.requestLayout()
            view.setTag(R.id.is_first_focus_up, true)
        } else {
            view?.error = message
        }
    }
}
