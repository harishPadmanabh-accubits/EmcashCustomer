package com.emcash.customerapp.extensions


import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import timber.log.Timber
import java.lang.Exception

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)

//fun <T : ViewModel> AppCompatActivity.obtainViewModelWithFactory(viewModelClass: Class<T>) =
//    ViewModelProviders.of(this,
//        Injection.provideViewModelFactory(application!!, applicationContext)).get(viewModelClass)

fun doLogWithTag(tag:String,message:String,type: LoggingKeys = LoggingKeys.ERROR){
    try{
        when(type){
            LoggingKeys.DEBUG ->Timber.tag(tag).d(message)
            LoggingKeys.ERROR ->Timber.tag(tag).e(message)
            LoggingKeys.INFO ->Timber.tag(tag).i(message)
        }
    }catch (e:Exception){
        Timber.e("Caught in doLogWithTag $e")
    }
}

fun <T : AndroidViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)

fun AppCompatActivity.hideToolbar() {
    supportActionBar?.hide()
}

fun AppCompatActivity.showToolbar() {
    supportActionBar?.show()
}

/**
 * Returns display density as ...DPI
 */
fun AppCompatActivity.getDisplayDensity(): String {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return when (metrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "LDPI"
        DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
        DisplayMetrics.DENSITY_HIGH -> "HDPI"
        DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
        DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
        else -> "XXHDPI"
    }
}

/**
 * Sets color to toolbar in AppCompatActivity
 */
fun AppCompatActivity.setToolbarColor(@ColorRes color: Int) {
    this.supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            ContextCompat.getColor(this, color)
        )
    )
}

/**
 * Perform replace for a support fragment
 */
inline fun AppCompatActivity.transact(action: FragmentTransaction.() -> Unit) {
    supportFragmentManager.beginTransaction().apply {
        action()
    }.commit()
}


enum class LoggingKeys {
    DEBUG, INFO, ERROR
}
