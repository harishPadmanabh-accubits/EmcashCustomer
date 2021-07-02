package com.emcash.customerapp.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders


fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)

fun <T : AndroidViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)