package com.emcash.customerapp.ui.newPayment.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.model.contacts.ContactsGroup
import timber.log.Timber

class ContactsPagingSource(
    val api:EmCashApis,
    var searchQuerry:String = ""
) :PagingSource<Int,ContactsGroup>(){
    override fun getRefreshKey(state: PagingState<Int, ContactsGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Timber.e("$anchorPosition anchor")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContactsGroup> {
        val nextPage = params.key ?: 1
        val response = api.getGroupedContacts(nextPage,10,searchQuerry)
        return LoadResult.Page(
            data = response.data.contactsGroups,
            prevKey = if (response.data.totalPages == 0) null else {
                if (nextPage == 1) null else nextPage - 1
            },
            nextKey = if (response.data.totalPages == 0) null else {
                if (nextPage == response.data.totalPages) null else response.data.page.plus(1)
            }
        )
    }
}