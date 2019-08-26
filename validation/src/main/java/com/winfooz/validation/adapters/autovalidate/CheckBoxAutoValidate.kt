package com.winfooz.validation.adapters.autovalidate

import android.widget.CheckBox
import com.winfooz.validation.AutoValidate
import com.winfooz.validation.OnValueChanged

/**
 * Project: WinValidation
 * Created: Aug 11, 2019
 *
 * @author Mohamed Hamdan
 */
class CheckBoxAutoValidate(onValueChanged: OnValueChanged) :
    AutoValidate<CheckBox>(onValueChanged) {

    override fun start(view: CheckBox?) {
        view?.setOnCheckedChangeListener { _, _ ->
            onValueChanged()
        }
    }
}
