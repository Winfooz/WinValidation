@file:JvmName("ViewUtils")

package com.winfooz.validation.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.winfooz.validation.R

/**
 * Project: TruckingAndroid
 * Created: Jul 30, 2019
 *
 * @author Mohamed Hamdan
 */
internal inline fun EditText.onTextChanged(crossinline callback: (text: CharSequence?) -> Unit) {
    val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            // No impl
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No impl
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s)
        }
    }
    addTextChangedListener(textWatcher)
    setTag(R.id.edit_text_watcher, textWatcher)
}
