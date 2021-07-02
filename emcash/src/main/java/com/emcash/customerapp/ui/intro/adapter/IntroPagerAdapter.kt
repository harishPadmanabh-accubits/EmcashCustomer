package com.emcash.customerapp.ui.intro.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emcash.customerapp.ui.intro.FirstIntroFragment
import com.emcash.customerapp.ui.intro.FourthIntroFragment
import com.emcash.customerapp.ui.intro.SecondIntroFragment
import com.emcash.customerapp.ui.intro.ThirdIntroFragment
import java.lang.Exception

class IntroPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->FirstIntroFragment()
           1->SecondIntroFragment()
           2->ThirdIntroFragment()
           3->FourthIntroFragment()
           else->throw Exception("Invalid Position")
       }
    }
}