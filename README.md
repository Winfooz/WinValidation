# WinValidation
A light-weight android library that can be quickly integrated into any app to use user input validations.

- Annotations based.
- Auto validation handling.
- Auto find view by id.
- Support custom annotations.
- Support custom confirmation annotations e.g (Password and Confirm password).
- Support auto validation while typing.
- Support multi module projects.
- Support Androidx.
- Support auto validation for custom views.

## Setup
[ ![Download](https://api.bintray.com/packages/mnayef95/WinValidation/validation/images/download.svg) ](https://bintray.com/mnayef95/WinValidation/validation/_latestVersion)

```yaml
repositories {
    maven {
        url  "https://dl.bintray.com/mnayef95/WinValidation" 
    }
}
```

<details open>
<summary>Kotlin</summary>

```yaml
implementation 'com.winfooz:validation:{latest-version}'
kapt 'com.winfooz:validation-compiler:{latest-version}'
```
</details>

<details>
<summary>Java</summary>

```yaml
implementation 'com.winfooz:validation:{latest-version}'
annotationProcessing 'com.winfooz:validation-compiler:{latest-version}'
```
</details>

# How to use
```kotlin
@Validations
class TestActivity : Activity() {

    @NotEmpty(R.id.username, messageResId = R.string.err_username_validation)
    var editTextUsername: TextInputEditText? = null

    @Password(R.id.password, messageResId = R.string.err_password_validation)
    var editTextPassword: TextInputEditText? = null

    @ConfirmPassword(R.id.confirm_password, messageResId = R.string.err_confirm_password_validation)
    var editTextConfirmPassword: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        
        // Don't forget to bind your activity, fragment or view
        Validator.bind(this)
    }

    @ValidateOnClick(R.id.buttonActivityMainSubmit)
    fun onSubmitClicked() {
        Toast.makeText(this, "Validation succeeded", Toast.LENGTH_SHORT).show()
    }
}
```

# Auto validate while typing
```kotlin
@Validations
class TestActivity : Activity() {

    @AutoValidate
    @NotEmpty(R.id.username, messageResId = R.string.err_username_validation)
    var editTextUsername: TextInputEditText? = null
    
    ....
}
```
## Library Projects
For library projects add [Butterknife's gradle plugin](https://github.com/JakeWharton/butterknife#library-projects) to your `buildscript`.

```yaml
buildscript {
  repositories {
    mavenCentral()
   }
  dependencies {
    classpath 'com.jakewharton:butterknife-gradle-plugin:10.1.0'
  }
}
```

and then apply it in your module:
```yaml
apply plugin: 'com.android.library'
apply plugin: 'com.jakewharton.butterknife'
```

Now make sure you use R2 instead of R inside all WinValidation annotations.
```kotlin
@Validations
class TestActivity : Activity() {

    @AutoValidate
    @NotEmpty(R2.id.username, messageResId = R2.string.err_username_validation)
    var editTextUsername: TextInputEditText? = null
    
    ....
}
```
## License
WinValidation is released under the MIT license. [See LICENSE](https://github.com/Winfooz/WinValidation/blob/master/LICENSE) for details.
