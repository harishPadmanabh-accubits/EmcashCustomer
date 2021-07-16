package com.emcash.customerapp.model

data class DummyAcivityModel(
    val date: String,
    val activities: List<DummyActivityDetails>
) {

}

data class DummyActivityDetails(
    val type: Int,
    val valueLoaded: String,
    val time: String,
    val changedValue: String,
    val Balance: String
) {

}

const val ACTIVITY_TYPE_LOADED = 200
const val ACTIVITY_TYPE_CONVERTED = 201


val dummyActivityDetails1 = listOf<DummyActivityDetails>(
    DummyActivityDetails(ACTIVITY_TYPE_LOADED, "30", "9:00 PM", "+30", "40"),
    DummyActivityDetails(ACTIVITY_TYPE_CONVERTED, "10", "9:00 PM", "AED 10", "30")

)

val dummyActivityData = listOf<DummyAcivityModel>(
    DummyAcivityModel("19 May 2021", dummyActivityDetails1),
    DummyAcivityModel("19 May 2021", dummyActivityDetails1),
    DummyAcivityModel("19 May 2021", dummyActivityDetails1),
    DummyAcivityModel("19 May 2021", dummyActivityDetails1)


)

data class DummyTransactionModel(
    val date: String,
    val transactionList: List<DummyTransactionDetalsModel>

)

data class DummyTransactionDetalsModel(
    val type: Int,
    val valueLoaded: String,
    val time: String,
    val Balance: String
)

const val TRANSACTED_INBOUND = 300
const val TRANSACTED_OUTBOUND = 301


val dummyTransactionDetailsList = listOf<DummyTransactionDetalsModel>(
    DummyTransactionDetalsModel(TRANSACTED_INBOUND, "30", "09:00 PM", "15"),
    DummyTransactionDetalsModel(TRANSACTED_OUTBOUND, "10", "10:25 AM", "5"),
    DummyTransactionDetalsModel(TRANSACTED_INBOUND, "30", "09:00 PM", "15"),
    DummyTransactionDetalsModel(TRANSACTED_OUTBOUND, "30", "09:00 PM", "35"),
    DummyTransactionDetalsModel(TRANSACTED_OUTBOUND, "30", "10:25 AM", "15"),
    DummyTransactionDetalsModel(TRANSACTED_OUTBOUND, "30", "09:00 PM", "15")
  )

val dummyTransactionHistoryData = listOf<DummyTransactionModel>(
    DummyTransactionModel("19 May 2021", dummyTransactionDetailsList),
    DummyTransactionModel("20 May 2021", dummyTransactionDetailsList)

)