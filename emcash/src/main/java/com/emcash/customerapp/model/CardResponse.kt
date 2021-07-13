package com.emcash.customerapp.model

data class DummyCardResponse(
    val id: Int,
    val cardnumber: String,
    val amount: String,
    var default: Boolean
)

val dummyAccounts = arrayListOf<DummyCardResponse>(
    DummyCardResponse(1, "Empay - Prepaid- XXXXX 145", "AED 234.20", true),
    DummyCardResponse(2, "Empay - Credit- XXXXX 607", "AED 234.20", false)
)

