package com.emcash.customerapp.model

import android.icu.text.CaseMap
import com.emcash.customerapp.R
import com.emcash.customerapp.utils.LevelProfileImageView
import com.emcash.customerapp.utils.LevelProfileImageView.UserProfileLevel
import com.emcash.customerapp.utils.LevelProfileImageView.UserProfileLevel.*

data class DummyContactsRawData(
    var name:String,
    var level: UserProfileLevel,
    var number : String,
    var image:Int?  //to be changed to string from response
)

val dummyContactList = arrayListOf<DummyContactsRawData>(
    DummyContactsRawData("Abd al-Rahman", YELLOW,"+62 898 787 78", R.drawable.tbd_sample_dp_2),
    DummyContactsRawData("Abu Abdullah", RED,"+62 898 787 78", null),
    DummyContactsRawData("Barkatullah", GREEN,"+62 898 787 78", R.drawable.tbd_sample_dp_2),
    DummyContactsRawData("Barkat", YELLOW,"+62 898 787 78", R.drawable.tbd_sample_dp_2),
    DummyContactsRawData("Jamid", YELLOW,"+62 898 787 78", null)
    )

val dummyRecentContacts = users

data class GroupedContacts(
    var letter : String,
    var contacts : ArrayList<DummyContactsRawData>
)

data class ContactsPageItems(
    val title: String,
    var type : Int
){
    var recentContactList : ArrayList<DummyUserData> ?=null
    var allContactList : ArrayList<GroupedContacts>?=null
}