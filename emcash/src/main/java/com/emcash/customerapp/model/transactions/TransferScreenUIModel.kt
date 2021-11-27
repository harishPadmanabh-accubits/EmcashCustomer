package com.emcash.customerapp.model.transactions

data class TransferScreenUIModel(
    val userId:Int,
    val amount: Int,
    var desc:String="",
    var isEditable:Boolean = true
)
