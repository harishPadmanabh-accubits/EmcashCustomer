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
    val level: Int,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("roleId")
    val roleId: Int
)


data class ContactsGroupResponse(
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
        val contactsGroups: List<ContactsGroup>,
        @SerializedName("totalPages")
        val totalPages: Int
    )
}
data class ContactsGroup(
    @SerializedName("contacts")
    val contacts: List<ContactInfo>,
    @SerializedName("key")
    val letter: String
) {
    data class ContactInfo(
        @SerializedName("address")
        val address: Any?,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("fcmToken")
        val fcmToken: Any?,
        @SerializedName("guid")
        val guid: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("isRegistrationCompleted")
        val isRegistrationCompleted: Boolean,
        @SerializedName("name")
        val name: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("ppp")
        val level: Int,
        @SerializedName("profileImage")
        val profileImage: String?,
        @SerializedName("qrCode")
        val qrCode: Any?,
        @SerializedName("roleId")
        val roleId: Int,
        @SerializedName("status")
        val status: Any?,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("zipCode")
        val zipCode: Any?
    )
}