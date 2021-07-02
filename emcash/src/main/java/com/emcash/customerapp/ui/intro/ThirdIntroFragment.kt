package com.emcash.customerapp.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import kotlinx.android.synthetic.main.fragment_first_intro.*

class ThirdIntroFragment : Fragment() {
    private lateinit var viewModel: IntroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_intro, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        btn_next.setOnClickListener {
            viewModel._screenPosition.value = 3
        }

    }

    private fun initViewModel() {
        viewModel = requireActivity().obtainViewModel(IntroViewModel::class.java)
    }

}