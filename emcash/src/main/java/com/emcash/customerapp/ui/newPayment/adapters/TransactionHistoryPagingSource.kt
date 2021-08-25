package com.emcash.customerapp.ui.newPayment.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcash.customerapp.data.network.EmCashApis
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.payments.TransactionHistory
import com.emcash.customerapp.model.payments.TransactionHistoryUI
import timber.log.Timber

class TransactionHistoryPagingSource(
    val api: EmCashApis,
    val userId:Int
):PagingSource<Int,TransactionHistoryUI>() {
    override fun getRefreshKey(state: PagingState<Int, TransactionHistoryUI>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Timber.e("$anchorPosition anchor")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionHistoryUI> {
        val nextPage = params.key ?: 1
        val response = api.getTransactionHistory(userId,nextPage,10)
        val alltransactions = response.body()?.data?.transactions
        val groupedTransactions = groupPaymentTransactionsByDate(ArrayList(alltransactions))
        return LoadResult.Page(
            data = groupedTransactions.asReversed(),
            prevKey = if (nextPage == 1) null else nextPage - 1,
            nextKey =if(nextPage==response?.body()?.data?.totalPages) null else response?.body()?.data?.page?.plus(1)
        )
    }


    fun groupPaymentTransactionsByDate(rows: ArrayList<TransactionHistory>?): ArrayList<TransactionHistoryUI> {

        val paymentActivityList = ArrayList<TransactionHistoryUI>()  //final processed list
        val accessedDates = ArrayList<String>() //to check if a date is alreaaady accesssed

        rows?.let { allTransactions ->   //check if rows are null
            if (!allTransactions.isNullOrEmpty()) {
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
                        paymentActivityList.add(
                            TransactionHistoryUI(
                                formattedDate,
                                groupedActivities
                            )
                        )  //add to custom model
                        accessedDates.add(formattedDate)               //add date to accessDate Array
                    }


                }
            }


        }
        return paymentActivityList//pass this to adapter
    }

}