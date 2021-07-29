package com.emcash.customerapp.ui.newPayment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.hideKeyboard
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showKeyboard
import com.emcash.customerapp.ui.prepare.BottomSheetListener
import kotlinx.android.synthetic.main.activity_prepare_em_cash.*
import kotlinx.android.synthetic.main.bottom_sheet_how_it_works.*
import kotlinx.android.synthetic.main.transfer_fragment.*
import kotlinx.android.synthetic.main.transfer_fragment.fl_bottom_sheet
import kotlinx.android.synthetic.main.activity_prepare_em_cash.fl_tint as fl_tint1

class TransferFragment:Fragment(R.layout.transfer_fragment) , BottomSheetListener {

    val viewModel:NewPaymentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_value.requestFocus()
        fab_transfer.setOnClickListener {
            viewModel.gotoScreen(NewPaymentScreens.PIN)
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        ll_info.setOnClickListener{
            viewModel._bottomSheetVisiblity.value = true
        }

        observe()
    }

    private fun observe() {
        viewModel.apply {
            bottomSheetVisibility.observe(viewLifecycleOwner, Observer {
                when (it) {
                    true -> showBottomSheet()
                    false -> closeBottomSheet()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().showKeyboard(et_value)
    }
    private fun showBottomSheet() {
        requireActivity().hideKeyboard()
        tv_info_desc.text = getString(R.string.send_token_rewards)
        val transition: Transition = Slide(Gravity.BOTTOM);
        transition.duration = 600
        transition.addTarget(fl_bottom_sheet)
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(p0: Transition?) {
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
                showTint()
            }
        })
        TransitionManager.beginDelayedTransition(fl_bottom_sheet, transition)
        fl_bottom_sheet.show()
        btn_got_it.setOnClickListener {
            onGotitClicked()
        }

    }

    private fun closeBottomSheet() {
        val transition: Transition = Slide(Gravity.BOTTOM);
        transition.duration = 500
        transition.addTarget(fl_bottom_sheet)
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(p0: Transition?) {
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
                hideTint()
            }
        })
        TransitionManager.beginDelayedTransition(fl_bottom_sheet, transition)
        fl_bottom_sheet.hide()
    }

    fun showTint() {
        ObjectAnimator.ofFloat(fl_tint, "alpha", 90f).apply {
            duration = 500
            start()
        }
    }

    fun hideTint(){
        ObjectAnimator.ofFloat(fl_tint, "alpha", 0f).apply {
            duration = 500
            start()
        }
    }

    override fun onGotitClicked() {
        //closeBottomSheet()
        viewModel._bottomSheetVisiblity.value =false

    }

}