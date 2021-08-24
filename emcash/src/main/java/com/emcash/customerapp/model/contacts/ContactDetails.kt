package com.emcash.customerapp.model.contacts


import com.google.gson.annotations.SerializedName

data class ContactDetails(
    @SerializedName("data")
    val contact: Contact,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {

}
data class Contact(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isContactUserBlockedLoggedInUser")
    val isContactUserBlockedLoggedInUser: Boolean,
    @SerializedName("isLoggedInUserBlockedContactUser")
    val isLoggedInUserBlockedContactUser: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("ppp")
    val level: Int,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("roleId")
    val roleId: Int
)