package com.winfooz.validation.errorhandlingadapters

import android.widget.CheckBox
import com.winfooz.validation.ErrorHandling

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class CheckBoxErrorHandling : ErrorHandling<CheckBox, Annotation> {

    override fun handleError(view: CheckBox?, message: String?, annotation: Annotation?) {
        view?.error = message
    }
}
