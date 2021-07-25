package com.emcash.customerapp.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders


fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)

fun <T : AndroidViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)

fun FragmentActivity.hideKeyboard() {
    try {
        val inputManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    } catch (e: Exception) {
    }
}

fun FragmentActivity.showKeyboard(view: View?) {
    try {
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


