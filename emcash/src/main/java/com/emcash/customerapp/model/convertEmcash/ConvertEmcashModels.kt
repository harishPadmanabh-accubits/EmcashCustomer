package com.emcash.customerapp.model.convertEmcash

import com.google.gson.annotations.SerializedName

data class UserBankAccountResponse(
    @SerializedName("data")
    val `data`: Boolean,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class BankDetailsResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("beneficiaryName")
        val beneficiaryName: String,
        @SerializedName("branchName")
        val branchName: String,
        @SerializedName("iBanNumber")
        val iBanNumber: String,
        @SerializedName("nickName")
        val nickName: String,
        @SerializedName("branchCode")
        val branchCode: String,
        @SerializedName("swiftCode")
        val swiftCode: String,
        @SerializedName("userBankDetailsRefeId")
        val userBankDetailsRefeId: String,
        @SerializedName("userId")
        val userId: String
    )
}


data class AddBankDetailsRequest(
    @SerializedName("beneficiaryName")
    val beneficiaryName: String,
    @SerializedName("branchName")
    val branchName: String,
    @SerializedName("iBanNumber")
    val iBanNumber: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("swiftCode")
    val swiftCode: String,
    @SerializedName("branchCode")
    val branchCode: String
)