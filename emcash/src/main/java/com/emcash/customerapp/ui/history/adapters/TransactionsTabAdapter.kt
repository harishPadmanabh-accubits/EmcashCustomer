package com.emcash.customerapp.ui.history.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emcash.customerapp.ui.history.AllTransactionsFragment
import com.emcash.customerapp.ui.history.InboundTransactionsFragment
import com.emcash.customerapp.ui.history.OutBoundTransactionsFragment
import java.lang.Exception

class TransactionsTabAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0->{
                AllTransactionsFragment()
            }
            1->{
                InboundTransactionsFragment()
            }
            2->{
                OutBoundTransactionsFragment()
            }
            else -> throw Exception("Illegal position in Tab layout")
        }
    }

}