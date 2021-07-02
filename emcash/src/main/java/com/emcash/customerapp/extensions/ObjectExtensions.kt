package com.emcash.customerapp.extensions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.emcash.customerapp.BuildConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

fun Any.toJsonString():String{
    return Gson().toJson(this)
}

fun <T : Any> String.fromJson(destination:Class<T>): T {
    return Gson().fromJson(this,destination)
}

// add entry in shared preference
fun SharedPreferences.putAny(key: String, value: Any) {
    when (value) {
        is String -> edit().putString(key, value).apply()
        is Int -> edit().putInt(key, value).apply()
        is Boolean -> edit().putBoolean(key,value).apply()
        is Long -> edit().putLong(key,value).apply()
        else ->
            edit().putString(key,Gson().toJson(value)).apply()
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

fun Any.toJson():String{
    return Gson().toJson(this)
}

fun <T : Any>  String.FromJson(any:Class<T>): T {
    return Gson().fromJson(this,any)
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

fun EditText.onDeletePressed(changeFocusTo:EditText){
    this.setOnKeyListener { view, i, keyEvent ->
        if(i == KeyEvent.KEYCODE_DEL)
            changeFocusTo.requestFocus()
        return@setOnKeyListener false
    }
}

//set default value for live data
fun <T : Any?> MutableLiveData<T>.default(initialValue:T) = apply {
    try {
        setValue(initialValue)
    }catch (e:java.lang.Exception){

    }
}

//TOAST-short
fun Context.showShortToast(message:String?) = message?.let {message->
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//Toast-long
fun Context.showLongToast(message:String?) = message?.let {message->
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


//load imageView with image url
fun ImageView.loadImageWithUrl(imageUrl : String?){
    try{
        imageUrl?.let{imageUrl->
            if (context!=null){
                Glide.with(context)
                    .load(imageUrl)
                    .into(this)
            }

        }

    }catch (e:Exception){
        e.printStackTrace()
    }


}

//load imageView with image url
fun ImageView.loadImageWithUrl(imageUrl : String?,onError:(status:Boolean)->Unit){
    try{
        imageUrl?.let{imageUrl->
            if (context!=null){
                Glide.with(context)
                    .load(imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                            onError(true)
                            return false
                        }
                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            //do something when picture already loaded
                            onError(false)
                            return false
                        }
                    })
                    .into(this)
            }

        }

    }catch (e:Exception){
        e.printStackTrace()
    }


}

//load imageView with image drawable resource
fun ImageView.loadImageWithResId(resID : Int?) = try{

    resID?.let{resID->
        if(context!=null){
            Glide.with(context)
                .load(resID)
                .into(this)
        }
    }

}catch (e:Exception){
    e.printStackTrace()
}

//load imageView with image url  and error
fun ImageView.loadImageWithError(imageUrl: String?, errorResId : Int) = try{

    imageUrl?.let{imageUrl->
        Glide.with(context)
            .load(imageUrl)
            .error(errorResId)
            .into(this)
    }

}catch (e:Exception){
    e.printStackTrace()
}



//load imageView with image url  and @NON-NULL placeholder
fun ImageView.loadImageWithPlaceHolder(imageUrl: String?, placeHolderResId : Int) = try{

    imageUrl?.let{imageUrl->
        Glide.with(context)
            .load(imageUrl)
            .placeholder(placeHolderResId)
            .into(this)
    }

}catch (e:Exception){
    e.printStackTrace()
}

//hide view
fun View.hide() {
    visibility = View.GONE
}

//show view
fun View.show() {
    visibility = View.VISIBLE
}

//make view invisible
fun View.invible(){
    visibility = View.INVISIBLE
}
//
//fun <T : Any> Call<T>.awaitResponse(
//    onSuccess: (T?) -> Unit = {},
//    onFailure: (String?) -> Unit = {}
//) {
//
//    this.enqueue(object : Callback<T> {
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//            val r = response
//            if (response.isSuccessful) {
//                onSuccess.invoke(response.body())
//            } else {
//                val gson = Gson()
//                val (error, message, status) = gson.fromJson(
//                    response.errorBody()!!.charStream(),
//                    BaseResponse::class.java
//                ).also {
//                    onFailure.invoke(it.message)
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//            onFailure.invoke( t.message)
//        }
//    })
//}






