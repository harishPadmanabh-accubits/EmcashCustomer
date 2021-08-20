package com.emcash.customerapp.model.contacts


import com.google.gson.annotations.SerializedName

data class AllContactsResponse(
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
        @SerializedName("count")
        val count: Int,
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("page")
        val page: Int,
        @SerializedName("rows")
        val contactList: List<ContactItem>,
        @SerializedName("totalPages")
        val totalPages: Int
    ) 
}

data class ContactItem(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("ppp")
    val ppp: Int,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("roleId")
    val roleId: Int
)