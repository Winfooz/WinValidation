package com.winfooz.validation

import android.view.View
import com.winfooz.AnnotationsAutoValidate

/**
 * Project: WinValidation
 * Created: Aug 11, 2019
 *
 * @author Mohamed Hamdan
 */
typealias OnValueChanged = AutoValidate<*>.() -> Unit

abstract class AutoValidate<V : View>(val onValueChanged: OnValueChanged) :
    AnnotationsAutoValidate {

    abstract fun start(view: V?)
}
