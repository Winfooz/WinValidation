package com.winfooz.validation.sample

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.winfooz.annotations.AutoValidate
import com.winfooz.annotations.ValidateOnClick
import com.winfooz.annotations.Validations
import com.winfooz.validation.Validator
import com.winfooz.validation.annotations.NotEmpty
import com.winfooz.validation.annotations.Password

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Validations
class MainActivity : Activity() {

    @AutoValidate
    @NotEmpty(R.id.editTextActivityMainUsername, messageResId = R.string.err_username_validation)
    var editTextUsername: EditText? = null

    @AutoValidate
    @Password(R.id.editTextActivityMainPassword, messageResId = R.string.err_password_validation)
    var editTextPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Validator.bind(this)
    }

    @ValidateOnClick(R.id.buttonActivityMainSubmit)
    fun onSubmitClicked(boolean: Boolean) {
        Toast.makeText(this, "isValid: $boolean", Toast.LENGTH_SHORT).show()
    }
}
