package com.emcash.customerapp.extensions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.emcash.customerapp.*
import com.emcash.customerapp.data.network.exceptions.NoInternetException
import com.emcash.customerapp.enums.TransactionType
import com.emcash.customerapp.utils.ERROR_NO_INTERNET
import com.emcash.customerapp.utils.IMAGE_BASE_URL
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_inner_contact_details.view.*
import kotlinx.android.synthetic.main.transfer_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


private const val TIME_STAMP_FORMAT = "EEEE, MMMM d, yyyy - hh:mm:ss a"
private const val DATE_FORMAT = "yyyy-MM-dd"

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun Long.getTimeStamp(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

@Throws(ParseException::class)
fun String.getDateUnixTime(): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.parse(this)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}

fun Any?.isNull() = this == null

fun Any.toJsonString(): String {
    return Gson().toJson(this)
}

fun <T : Any> String.fromJson(destination: Class<T>): T {
    return Gson().fromJson(this, destination)
}

// add entry in shared preference
fun SharedPreferences.putAny(key: String, value: Any) {
    when (value) {
        is String -> edit().putString(key, value).apply()
        is Int -> edit().putInt(key, value).apply()
        is Boolean -> edit().putBoolean(key, value).apply()
        is Long -> edit().putLong(key, value).apply()
        else ->
            edit().putString(key, Gson().toJson(value)).apply()
    }
}

/**
 * Computes status bar height
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier(
        "status_bar_height", "dimen",
        "android"
    )
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}


/**
 * Computes screen height
 */
fun Context.getScreenHeight(): Int {
    var screenHeight = 0
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    wm?.let {
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
    }
    return screenHeight
}

/**
 * Convert dp integer to pixel
 */
fun Context.toPx(dp: Int): Float {
    val displayMetrics = this.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

/**
 * Get color from resources
 */
fun Context.getCompatColor(@ColorRes colorInt: Int): Int =
    ContextCompat.getColor(this, colorInt)

/**
 * Get drawable from resources
 */
fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableRes)

/**
 * Convert a given date to milliseconds
 */
fun Date.toMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}

/**
 * Checks if dates are same
 */
fun Date.isSame(to: Date): Boolean {
    val sdf = SimpleDateFormat("yyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(to)
}

/**
 * Converts raw string to date object using [SimpleDateFormat]
 */
fun String.convertStringToDate(simpleDateFormatPattern: String): Date? {
    val simpleDateFormat = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
    var value: Date? = null
    justTry {
        value = simpleDateFormat.parse(this)
    }
    return value
}

/**
 * Wrapping try/catch to ignore catch block
 */
inline fun <T> justTry(block: () -> T) = try {
    block()
} catch (e: Throwable) {
}

/**
 * App's debug mode
 */
inline fun debugMode(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

/**
 * For functionality supported above API 21 (Eg. Material design stuff)
 */
inline fun lollipopAndAbove(block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        block()
    }
}


fun View.isVisibile(): Boolean = this.visibility == View.VISIBLE

fun View.isGone(): Boolean = this.visibility == View.GONE

fun View.isInvisible(): Boolean = this.visibility == View.INVISIBLE

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}


fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> Context.openActivity(it: Class<T>) {
    val intent = Intent(this, it)
    startActivity(intent)
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

fun <T : Any> String.FromJson(any: Class<T>): T {
    return Gson().fromJson(this, any)
}

//Text Watcher for edit text
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.afterTextChanged(
    afterTextChanged: (String) -> Unit,
    beforeTextChanged: (String) -> Unit
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChanged.invoke(p0.toString())
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.onDeletePressed(changeFocusTo: EditText, function: () -> Unit) {
    this.setOnKeyListener { view, i, keyEvent ->
        if (i == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
            function.invoke()
            changeFocusTo.requestFocus()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}

fun EditText.onDeletePressed(function: () -> Unit) {
    this.setOnKeyListener { view, i, keyEvent ->
        if (i == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
            function.invoke()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}

//set default value for live data
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply {
    try {
        setValue(initialValue)
    } catch (e: java.lang.Exception) {

    }
}

//TOAST-short
fun Context.showShortToast(message: String?) = message?.let { message ->
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//Toast-long
fun Context.showLongToast(message: String?) = message?.let { message ->
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


//load imageView with image url
fun ImageView.loadImageWithUrl(imageUrl: String?) {
    try {
        imageUrl?.let { imageUrl ->
            if (context != null) {
                Glide.with(context)
                    .load(imageUrl)
                    .into(this)
            }

        }

    } catch (e: Exception) {
        e.printStackTrace()
    }


}

//load imageView with image url
fun ImageView.loadImageWithUrl(imageUrl: String?, onError: (status: Boolean) -> Unit) {
    try {
        imageUrl?.let { imageUrl ->
            if (context != null) {
                Glide.with(context)
                    .load(imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: Boolean
                        ): Boolean {
                            onError(true)
                            return false
                        }

                        override fun onResourceReady(
                            p0: Drawable?,
                            p1: Any?,
                            p2: Target<Drawable>?,
                            p3: DataSource?,
                            p4: Boolean
                        ): Boolean {
                            //do something when picture already loaded
                            onError(false)
                            return false
                        }
                    })
                    .into(this)
            }

        }

    } catch (e: Exception) {
        e.printStackTrace()
    }


}

//load imageView with image drawable resource
fun ImageView.loadImageWithResId(resID: Int?) = try {

    resID?.let { resID ->
        if (context != null) {
            Glide.with(context)
                .load(resID)
                .into(this)
        }
    }

} catch (e: Exception) {
    e.printStackTrace()
}

//load imageView with image url  and error
fun ImageView.loadImageWithError(imageUrl: String?, errorResId: Int) = try {

    imageUrl?.let { imageUrl ->
        Glide.with(context)
            .load(imageUrl)
            .error(errorResId)
            .into(this)
    }

} catch (e: Exception) {
    e.printStackTrace()
}


//load imageView with image url  and @NON-NULL placeholder
//fun ImageView.loadImageWithPlaceHolder(imageUrl: String?, placeHolderResId: Int) = try {
//    imageUrl?.let { imageUrl ->
//        Glide.with(context)
//            .load(imageUrl)
//            .placeholder(placeHolderResId)
//            .into(this)
//    }
//
//} catch (e: Exception) {
//    e.printStackTrace()
//}

//hide view
fun View.hide() {
    visibility = View.GONE
}

//show view
fun View.show() {
    visibility = View.VISIBLE
}

//make view invisible
fun View.invible() {
    visibility = View.INVISIBLE
}

fun isNougatOrAbove(): Boolean =
    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N

fun isLollipopOrAbove(): Boolean =
    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N


fun <T : Any> Call<T>.awaitResponse(
    onSuccess: (T?) -> Unit = {},
    onFailure: (String?) -> Unit = {},
    onSessionExpired: () -> Unit = {},
    launchSource: Int = 0
) {

    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val r = response
            if (response.isSuccessful) {
                onSuccess.invoke(response.body())
            } else {
                if (response.code() == 401)
                    EmCashCommunicationHelper.getParentListener()
                        .onVerifyPin(TransactionType.VERIFY_USER, launchSource)

                onFailure.invoke(response.message())


            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (t is UnknownHostException || t is NoInternetException || t.message?.contains("Unable to resolve host") == true)
                onFailure.invoke(ERROR_NO_INTERNET)
            else
                onFailure.invoke(t.message)
        }
    })
}

fun Int.toRewardLevelString(context: Context): String {
    return when (this) {
        1 -> context.getString(R.string.reward_high)
        2 -> context.getString(R.string.reward_medium)
        3 -> context.getString(R.string.reward_low)
        else -> throw IllegalArgumentException("Invalid level constant received at toRewardLevelString()")
    }

}

fun ImageView.loadImageWithErrorCallback(
    url: String?, onError: () -> Unit = {}
) {

    Timber.e("Image Url ${IMAGE_BASE_URL.plus(url)}")
    Glide.with(this.context)
        .load(IMAGE_BASE_URL.plus(url))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .placeholder(R.color.ash_dark)
        .into(this)

}

fun ImageView.loadImageWithPlaceHolder(
    url: String?, placeHolderResId: Int, onError: () -> Unit = {}
) {

    Timber.e("Image Url ${IMAGE_BASE_URL.plus(url)}")
    Glide.with(this.context)
        .load(IMAGE_BASE_URL.plus(url))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .placeholder(placeHolderResId)
        .into(this)

}

fun String.toLocalDate(): String {
    val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS")
    sdfInput.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdfInput.parse(this)
    val sdfOutput = SimpleDateFormat("dd MMM yyyy")
    sdfOutput.timeZone = TimeZone.getDefault()
    val formatted = sdfOutput.format(date)
    return formatted
}

fun String.toLocalTime(): String {
    val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS")
    sdfInput.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdfInput.parse(this)
    val sdfOutput = SimpleDateFormat("hh:mm a")
    sdfOutput.timeZone = TimeZone.getDefault()
    return sdfOutput.format(date)
}

fun toFormattedDate(dateStr: String): String {
    val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS")
    sdfInput.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdfInput.parse(dateStr)
    val sdfOutput = SimpleDateFormat("dd MMM yyyy")
    sdfOutput.timeZone = TimeZone.getDefault()
    val formatted = sdfOutput.format(date)
    return formatted
}

fun toFormattedTime(dateStr: String): String? {
    Timber.e("Datext $dateStr")
    val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS")
    sdfInput.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdfInput.parse(dateStr)
    val sdfOutput = SimpleDateFormat("hh:mm a")
    sdfOutput.timeZone = TimeZone.getDefault()
    val formatted = sdfOutput.format(date)
    return formatted

}

fun FrameLayout.setlevel(level: Int) {
    when (level) {
        1 -> this.setBackgroundResource(R.drawable.green_round)
        2 -> this.setBackgroundResource((R.drawable.yellow_round))
        3 -> this.setBackgroundResource(R.drawable.red_round)
        else -> this.setBackgroundResource(0)
    }
}

fun trimID(string: String): String? {
    var output: String? = null
    try {
        output = string.substring(0, string.indexOf('-'))
    } catch (exception: java.lang.Exception) {

    }

    return output

}

fun getCurrentDate(): String {
    val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.getDefault())
    val sdfOutput = SimpleDateFormat("dd MMM yyyy")
    sdfOutput.timeZone = TimeZone.getTimeZone("Etc/UTC")
    val formatted = sdfOutput.format(Date())


    return formatted
}

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}


fun String.killBackStackAndNavigate(context: Context) {
    val newStack = Intent(context, Class.forName(this)).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(newStack)
}

fun Context.logoutFromEmCash() {
    EmCashCommunicationHelper.getFallBack().killBackStackAndNavigate(this)
}

fun Int.toAmount(): String {
    return DecimalFormat("0.00").format(this)
}

fun SwipeRefreshLayout.stopIfRefreshing() {
    if (this.isRefreshing)
        this.isRefreshing = false
}

fun EditText.alignCentre() {
    this.afterTextChanged {
        if (it.isNotEmpty())
            this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        else
            this.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }
}

fun Int.toNotificationCount(): String = if (this >= 1000) "999+" else this.toString()

fun Context.hasInternet():Boolean {
    val connectivityManager = this.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when{
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false

    }
}
