package com.winfooz.validation.adapters

import android.widget.CheckBox
import com.winfooz.validation.ValidationAdapter

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class CheckBoxAdapter : ValidationAdapter<CheckBox, Boolean> {

    override fun getData(view: CheckBox?): Boolean? {
        return view?.isChecked
    }
}
