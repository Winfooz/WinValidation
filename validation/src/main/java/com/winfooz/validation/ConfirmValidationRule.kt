package com.winfooz.validation

import android.view.View
import com.winfooz.AnnotationsValidationRule

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
interface ConfirmValidationRule<V : View, T, A : Annotation> : AnnotationsValidationRule {

    fun isValid(view: V?, data: T?, annotation: A?): Boolean
}
