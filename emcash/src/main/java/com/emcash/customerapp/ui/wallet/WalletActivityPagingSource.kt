package com.emcash.customerapp.ui.wallet

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.wallet.TransactionItemUiModel
import com.emcash.customerapp.model.wallet.WalletActivityResponse
import com.emcash.customerapp.model.wallet.WalletActivityResponse.Data
import com.emcash.customerapp.model.wallet.WalletActivityResponse.Data.*
import timber.log.Timber

class WalletActivityPagingSource(
    val api: EmCashApis
):PagingSource<Int, TransactionItemUiModel>(){
    override fun getRefreshKey(state: PagingState<Int, TransactionItemUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Timber.e("$anchorPosition anchor")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionItemUiModel> {
        // Start refresh at page 1 if undefined.
        val nextPage = params.key ?: 1
        val response = api.getWalletTransactions(nextPage,8).body()
        val groupedActivities = response?.data?.activities?.let { groupByDate(it).toList() } ?: listOf<TransactionItemUiModel>()
        return LoadResult.Page(
            data = groupedActivities,
            prevKey = if (nextPage == 1) null else nextPage - 1,
            nextKey =if(nextPage==response?.data?.totalPages) null else response?.data?.page?.plus(1)
        )

    }

    private fun groupByDate(allTransactions:List<WalletActivityItem>):ArrayList<TransactionItemUiModel>{
        val finalActivityList = ArrayList<TransactionItemUiModel>()  //final processed list
        val accessedDates = ArrayList<String>() //to check if a date is alreaaady accesssed
        if(allTransactions.isNotEmpty()){
            val dates = allTransactions.map {
                it.updatedAt                                                                 //gets a list of updatedAt from all data using map
            }
            dates.forEach { unformattedDate ->
                Timber.e("Unformatted date $unformattedDate")
                val formattedDate = toFormattedDate(unformattedDate)
                Timber.e("formatted date $formattedDate")

                if(!accessedDates.contains(formattedDate)){              //check if date already accessed otherwise duplications will occur
                    val groupedActivities = allTransactions.filter { row ->
                        toFormattedDate(row.updatedAt) == formattedDate
                    }                                                     // get all trnasaction under each item in dates list
                    finalActivityList.add(TransactionItemUiModel(formattedDate, groupedActivities))  //add to custom model
                    accessedDates.add(formattedDate)               //add date to accessDate Array
                }


            }
        }
        return finalActivityList //pass this to adapter
    }

}