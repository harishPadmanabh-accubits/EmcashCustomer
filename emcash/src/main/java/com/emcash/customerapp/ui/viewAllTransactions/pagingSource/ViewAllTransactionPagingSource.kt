package com.emcash.customerapp.ui.viewAllTransactions.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.model.RecentTransactionsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import timber.log.Timber

class ViewAllTransactionPagingSource(val api: EmCashApis) :
    PagingSource<Int, RecentTransactionsResponse.Data.Row>() {
    override fun getRefreshKey(state: PagingState<Int, RecentTransactionsResponse.Data.Row>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecentTransactionsResponse.Data.Row> {
        val nextPage = params.key ?: FIRST_PAGE_INDEX
        val response = api.pagingAllTransaction(nextPage,10).body()
        var groupedActivities= listOf<RecentTransactionsResponse.Data.Row>()
        response?.data?.rows.let {
            if (it != null) {
                groupedActivities = it
            }

        }

        return LoadResult.Page(
            data = groupedActivities,
            prevKey = if (response?.data?.totalPages == 0) null else {
                if (nextPage == 1) null else nextPage - 1
            },
            nextKey = if (response?.data?.totalPages == 0) null else {
                if (nextPage == response?.data?.totalPages) null else response?.data?.page?.plus(1)

            }
        )
    }

    companion object
    {
        private const val FIRST_PAGE_INDEX=1
    }
}