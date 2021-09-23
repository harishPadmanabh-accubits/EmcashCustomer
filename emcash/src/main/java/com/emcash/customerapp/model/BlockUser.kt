package com.emcash.customerapp.model

import com.google.gson.annotations.SerializedName


data class BlockedResponse(
    @SerializedName("data")
    val `data`: Any,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class UnblockedResponse(
    @SerializedName("data")
    val `data`: Any,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class BlockObserverModel(
    var status:Boolean,
    var type: BlockType
)

enum class BlockType{
    BLOCK,UNBLOCK
}