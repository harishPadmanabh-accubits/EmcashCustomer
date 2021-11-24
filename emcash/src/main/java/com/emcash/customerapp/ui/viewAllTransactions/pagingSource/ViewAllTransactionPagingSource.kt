package com.emcash.customerapp.ui.viewAllTransactions.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.model.transactions.RecentTransactionItem

class ViewAllTransactionPagingSource(val api: EmCashApis) :
    PagingSource<Int, RecentTransactionItem>() {
    override fun getRefreshKey(state: PagingState<Int, RecentTransactionItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecentTransactionItem> {
        return try {
            val nextPage = params.key ?: 1
            val response = api.getAllTransactedUsers(nextPage,10)
            LoadResult.Page(
                data = response.data.transactionList,
                prevKey = if (response.data.totalPages == 0) null else {
                    if (nextPage == 1) null else nextPage - 1
                },
                nextKey = if (response.data.totalPages == 0) null else {
                    if (nextPage == response.data.totalPages) null else response.data.page.plus(1)

                }
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }

    }

}