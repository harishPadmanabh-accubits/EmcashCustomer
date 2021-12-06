package com.emcash.customerapp.model.notifications


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class NotificationClickResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: String,
    @SerializedName("data") val `data`: Data
) {
    @Keep
    data class Data(
        @SerializedName("id") val id: String,
        @SerializedName("userId") val userId: String,
        @SerializedName("message") val message: String,
        @SerializedName("type") val type: Int,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("contactUserId") val contactUserId: String,
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("updatedAt") val updatedAt: String
    )
}