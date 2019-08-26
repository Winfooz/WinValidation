package com.winfooz.validation

import com.winfooz.AnnotationsValidationRule

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
interface ValidationRule<T, A : Annotation> : AnnotationsValidationRule {

    fun isValid(data: T?, annotation: A?): Boolean
}
