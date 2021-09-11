package com.emcash.customerapp.ui.history.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.model.transactions.TransactionHistoryGroupResponse
import com.emcash.customerapp.model.transactions.TransactionHistoryGroupResponse.Data.TransactionGroup
import timber.log.Timber

class HistoryPagingSource(
    val api: EmCashApis,
    val mode: String,
    var startDate: String,
    var endDate: String,
    var status: String,
    var type: String
) :
    PagingSource<Int, TransactionGroup>() {
    override fun getRefreshKey(state: PagingState<Int, TransactionGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Timber.e("$anchorPosition anchor")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionGroup> {
        val nextPage = params.key ?: 1
        val response = api.getGroupedTransactionHistory(
            nextPage,
            10,
            mode,
            startDate,
            endDate, status,
            type
        )
        return LoadResult.Page(
            data = response.data.transactionGroup,
            prevKey = if (response.data.totalPages == 0) null else {
                if (nextPage == 1) null else nextPage - 1
            },
            nextKey = if (response.data.totalPages == 0) null else {
                if (nextPage == response.data.totalPages) null else response.data.page.plus(1)

            }

        )

    }
}