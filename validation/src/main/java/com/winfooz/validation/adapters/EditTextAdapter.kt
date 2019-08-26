package com.winfooz.validation.adapters

import android.widget.EditText
import com.winfooz.validation.ValidationAdapter

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
class EditTextAdapter : ValidationAdapter<EditText, CharSequence> {

    override fun getData(view: EditText?): CharSequence? {
        return view?.text
    }
}
