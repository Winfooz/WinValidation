package com.winfooz.validation

import android.view.View
import com.winfooz.AnnotationsValidateAdapter

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
interface ValidationAdapter<V : View, T> : AnnotationsValidateAdapter {

    fun getData(view: V?): T?
}
