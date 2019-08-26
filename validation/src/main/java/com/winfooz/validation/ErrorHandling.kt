package com.winfooz.validation

import android.view.View
import com.winfooz.AnnotationsErrorHandlingAdapter

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
interface ErrorHandling<V : View, A : Annotation> : AnnotationsErrorHandlingAdapter {

    fun handleError(view: V?, message: String?, annotation: A?)
}
