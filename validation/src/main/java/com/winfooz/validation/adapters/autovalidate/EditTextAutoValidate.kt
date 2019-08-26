package com.winfooz.validation.adapters.autovalidate

import android.widget.EditText
import com.winfooz.validation.AutoValidate
import com.winfooz.validation.OnValueChanged
import com.winfooz.validation.R
import com.winfooz.validation.utils.onTextChanged

/**
 * Project: WinValidation
 * Created: Aug 11, 2019
 *
 * @author Mohamed Hamdan
 */
class EditTextAutoValidate(onValueChanged: OnValueChanged) :
    AutoValidate<EditText>(onValueChanged) {

    private lateinit var editText: EditText

    override fun start(view: EditText?) {
        this.editText = view!!
        handleEditTextFocusListener()
        handleEditTextChangeListener()
    }

    private fun handleEditTextFocusListener() {
        editText.setTag(R.id.is_first_focus_up, false)
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                editText.setTag(R.id.is_first_focus_up, true)
                editText.onFocusChangeListener = null
                onValueChanged(this)
            }
        }
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
    }

    private fun handleEditTextChangeListener() {
        editText.onTextChanged {
            if (editText.getTag(R.id.is_first_focus_up) as Boolean) {
                onValueChanged(this)
            }
        }
    }
}
