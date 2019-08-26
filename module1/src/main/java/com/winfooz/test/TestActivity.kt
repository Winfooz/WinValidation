package com.winfooz.test

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.winfooz.annotations.AutoValidate
import com.winfooz.annotations.ValidateOnClick
import com.winfooz.annotations.Validations
import com.winfooz.validation.Validator
import com.winfooz.validation.annotations.Checked
import com.winfooz.validation.annotations.ConfirmPassword
import com.winfooz.validation.annotations.NotEmpty
import com.winfooz.validation.annotations.Password

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Validations
class TestActivity : Activity() {

    @AutoValidate
    @NotEmpty(R2.id.editTextActivityMainUsername, messageResId = R2.string.err_username_validation)
    var editTextUsername: TextInputEditText? = null

    @AutoValidate
    @Password(
        R2.id.editTextActivityMainPassword,
        messageResId = R2.string.err_password_validation,
        scheme = Password.Scheme.ALPHA_NUMERIC_ALLOW_SYMBOLS_WITHOUT_SPACE,
        min = 8
    )
    var editTextPassword: TextInputEditText? = null

    @AutoValidate
    @ConfirmPassword(
        R2.id.editTextActivityMainConfirmPassword,
        messageResId = R2.string.err_confirm_password_validation
    )
    var editTextConfirmPassword: TextInputEditText? = null

    @AutoValidate
    @Checked(R2.id.checkBoxActivityMainTermsAndConditions, messageResId = R2.string.err_terms_and_conditions_validation)
    var checkBoxTermsAndConditions: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Validator.bind(this)
    }

    @ValidateOnClick(R2.id.buttonActivityMainSubmit)
    fun onSubmitClicked() {
        Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show()
    }
}
