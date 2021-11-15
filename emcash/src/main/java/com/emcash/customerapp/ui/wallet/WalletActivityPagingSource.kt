package com.emcash.customerapp.ui.wallet

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.wallet.TransactionItemUiModel
import com.emcash.customerapp.model.wallet.WalletActivityGroupResponse
import com.emcash.customerapp.model.wallet.WalletActivityGroupResponse.Data.*
import com.emcash.customerapp.model.wallet.WalletActivityResponse
import com.emcash.customerapp.model.wallet.WalletActivityResponse.Data
import com.emcash.customerapp.model.wallet.WalletActivityResponse.Data.*
import com.emcash.customerapp.model.wallet.WalletActivityUIModel
import timber.log.Timber
import java.lang.Exception

class WalletActivityPagingSource(
    val api: EmCashApis
) : PagingSource<Int, WalletActivityGroup>() {
    override fun getRefreshKey(state: PagingState<Int, WalletActivityGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Timber.e("$anchorPosition anchor")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WalletActivityGroup> {
        return try {
            val nextPage = params.key ?: 1
            val response = api.getWalletGropedTransactions(nextPage, 8)
            LoadResult.Page(
                data = response.data.walletActivities,
                prevKey = if (response.data.totalPages == 0) null else {
                    if (nextPage == 1) null else nextPage - 1
                },
                nextKey = if (response.data.totalPages == 0) null else {
                    if (nextPage == response.data.totalPages) null else response.data.page.plus(1)

                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}