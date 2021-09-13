package com.emcash.customerapp.ui.newPayment.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.payments.TransactionGroupResponse
import com.emcash.customerapp.model.payments.TransactionGroupResponse.Data.TransactionGroup
import com.emcash.customerapp.model.payments.TransactionHistory
import com.emcash.customerapp.model.payments.TransactionHistoryUI
import timber.log.Timber
import java.lang.Exception

class TransactionHistoryPagingSource(
    val api: EmCashApis,
    val userId:Int
):PagingSource<Int, TransactionGroup>() {
    override fun getRefreshKey(state: PagingState<Int, TransactionGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Timber.e("$anchorPosition anchor")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionGroup> {
        return try {
            val nextPage = params.key ?: 1
            val response = api.getContactTransactionPagedList(userId,nextPage,10)
            LoadResult.Page(
                data = response.data.transactionGroups,
                prevKey = if (response.data.totalPages == 0) null else {
                    if (nextPage == 1) null else nextPage - 1
                },
                nextKey = if (response.data.totalPages == 0) null else {
                    if (nextPage == response.data.totalPages) null else response.data.page.plus(1)

                })
        }catch (e:Exception){
            LoadResult.Error(e)
        }

    }
}