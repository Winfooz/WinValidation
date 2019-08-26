package com.winfooz.validation

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import java.lang.reflect.Constructor

/**
 * Project: WinValidation
 * Created: Aug 10, 2019
 *
 * @author Mohamed Hamdan
 */
@Suppress("UNCHECKED_CAST")
object Validator {

    private const val TAG = "WinValidation"

    @JvmStatic
    fun bind(target: Activity) {
        return bind(target, target.window.decorView)
    }

    @JvmStatic
    fun bind(target: View) {
        return bind(target, target)
    }

    @JvmStatic
    fun bind(target: Dialog) {
        return bind(target, target.window?.decorView)
    }

    @JvmStatic
    fun bind(target: Any, source: Activity) {
        bind(target, source.window.decorView)
    }

    @JvmStatic
    fun bind(target: Any, source: Dialog) {
        bind(target, source.window?.decorView)
    }

    @JvmStatic
    fun bind(target: Any, source: View?) {
        val constructor = findBindingConstructorForClass(target.javaClass)
        try {
            constructor?.newInstance(target, source)
        } catch (e: Exception) {
            Log.d(TAG, "Unable to instantiate " + target.javaClass.name, e)
        }
    }

    @JvmStatic
    fun isValid(target: Activity): Boolean {
        return isValid(target, target.window.decorView)
    }

    @JvmStatic
    fun isValid(target: View): Boolean {
        return isValid(target, target)
    }

    @JvmStatic
    fun isValid(target: Dialog): Boolean {
        return isValid(target, target.window?.decorView)
    }

    @JvmStatic
    fun isValid(target: Any, source: Activity): Boolean {
        return isValid(target, source.window.decorView)
    }

    @JvmStatic
    fun isValid(target: Any, source: Dialog): Boolean {
        return isValid(target, source.window?.decorView)
    }

    @JvmStatic
    private fun isValid(target: Any, source: View?): Boolean {
        val constructor = findBindingConstructorForClass(target.javaClass)
        val validationInstance = constructor?.newInstance(target, source)

        val validateMethod = validationInstance?.javaClass?.getDeclaredMethod(
            "validate",
            Context::class.java,
            target.javaClass
        )
        validateMethod?.isAccessible = true
        return validateMethod?.invoke(validationInstance, source?.context, target) == true
    }

    @Suppress("UNCHECKED_CAST")
    private fun findBindingConstructorForClass(cls: Class<*>?): Constructor<*>? {
        val constructor: Constructor<*>?
        val clsName = cls?.name
        constructor = try {
            val bindingClass = cls?.classLoader?.loadClass(clsName + "_Validation")
            bindingClass?.getConstructor(cls, View::class.java) as Constructor<*>
        } catch (e: ClassNotFoundException) {
            Log.d(
                TAG,
                "Unable to find Validation class for " + cls?.name + " trying super class" + cls?.superclass?.name,
                e
            )
            findBindingConstructorForClass(cls?.superclass)
        } catch (e: NoSuchMethodException) {
            try {
                val bindingClass = cls?.classLoader?.loadClass(clsName + "_Validation")
                bindingClass?.getDeclaredConstructor() as Constructor<*>
            } catch (e: Exception) {
                Log.d(TAG, "Unable to find Validation class for " + cls?.name, e)
                return null
            }
        } catch (e: Exception) {
            Log.d(TAG, "Unable to find Validation class for " + cls?.name, e)
            return null
        }
        return constructor
    }
}
