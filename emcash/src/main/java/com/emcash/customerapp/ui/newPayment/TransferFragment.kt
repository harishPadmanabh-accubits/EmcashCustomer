package com.emcash.customerapp.ui.newPayment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.enums.TransactionType
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.contacts.Contact
import com.emcash.customerapp.ui.prepare.BottomSheetListener
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.bottom_sheet_how_it_works.*
import kotlinx.android.synthetic.main.transfer_fragment.*
import timber.log.Timber


class TransferFragment : Fragment(R.layout.transfer_fragment), BottomSheetListener {

    val viewModel: NewPaymentViewModel by activityViewModels()
    val contactBundle by lazy { arguments }
    val loader by lazy { LoaderDialog(requireContext()) }
    var userId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_transfer.setOnClickListener {
            if (fab_transfer.text.equals(TYPE_TRANSFER))
                transfer()
            else if (fab_transfer.text.equals(TYPE_REQUEST))
                request()
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        ll_info.setOnClickListener {
            viewModel._bottomSheetVisiblity.value = true
        }

        getContactDetails()

        observe()

    }



    private fun transfer() {
        val amount =
            if (et_value.text.toString().isNotEmpty()) et_value.text.toString().toInt() else 0
        if (amount > 0) {
            viewModel.initPayment(
                amount,
                userId,
                et_description.text.toString(),
                onInit = { status, refId, error ->
                    when (status) {
                        true -> {
                            val bundle = bundleOf(
                                KEY_TRANSACTION_TYPE to TransactionType.TRANSFER
                            )
                            viewModel.gotoScreen(NewPaymentScreens.PIN, bundle)
                        }
                        false -> {
                            requireActivity().showShortToast(error)
                        }
                    }
                })

        } else {
            requireActivity().showShortToast("Please enter a valid amount to transfer")
        }

    }

    fun request() {
        val amount =
            if (et_value.text.toString().isNotEmpty()) et_value.text.toString().toInt() else 0
        if (amount > 0) {
            loader.showLoader()
            viewModel.requestPayment(
                amount,
                userId,
                et_description.text.toString(),
                onRequest = { status, refId, error ->
                    when (status) {
                        true -> {
                            loader.hideLoader()
                            viewModel.gotoScreen(NewPaymentScreens.RECEIPT)
                        }
                        false -> {
                            loader.hideLoader()
                            requireActivity().showShortToast(error)
                        }
                    }
                })

        } else {
            requireActivity().showShortToast("Please enter a valid amount to transfer")
        }

    }

    private fun getContactDetails() {
        contactBundle?.let {
            val contact = it[KEY_SELECTED_CONTACT].toString().toInt() ?: 0
            val type = it[KEY_TRANSACTION_TYPE].toString()
            setupFab(type)
            Timber.e("Contact id in transfer screen $contact type $type")
            userId = contact
            viewModel.beneficiaryId = userId
            viewModel.getContactDetails(contact).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ApiCallStatus.SUCCESS -> {
                        loader.hideLoader()
                        renderDetails(it.data)
                        val activity = requireActivity() as NewPaymentActivity
                        if (activity.isFromQR) {
                            val profileDataFromQR = activity.qrDataProfile
                            profileDataFromQR?.let {
                                renderTransactionDetails(it.amount.toString(), it.description)
                            }
                        }

                    }
                    ApiCallStatus.ERROR -> {
                        loader.hideLoader()
                        requireActivity().showShortToast(it.errorMessage)
                    }
                    ApiCallStatus.LOADING -> {
                        loader.showLoader()
                    }
                }
            })
        }
    }

    private fun setupFab(type: String) {
        fab_transfer.text = type
    }

    private fun renderTransactionDetails(amount: String, desc: String){
        et_value.apply {
            setText(amount)

            isEnabled = false
        }
        et_description.apply {
            if(desc.isNotEmpty()){
                setText(desc)
                isEnabled = false
            }
        }

    }

    private fun renderDetails(data: Contact?) {
        data?.let {
            fl_user_level.setlevel(it.level)
            iv_user_dp.loadImageWithPlaceHolder(
                it.profileImage,
                R.drawable.ic_profile_placeholder
            )
            tv_user_name.text = it.name
            tv_user_phone.text = it.phoneNumber
            handleBlocking(it.isContactUserBlockedLoggedInUser, it.isLoggedInUserBlockedContactUser)
        }
    }

    private fun handleBlocking(
        contactUserBlockedLoggedInUser: Boolean,
        loggedInUserBlockedContactUser: Boolean
    ) {
        if (!contactUserBlockedLoggedInUser && !loggedInUserBlockedContactUser)
            fab_transfer.show()
        else {
            fab_transfer.hide()
            requireActivity().showShortToast("This contact has been blocked")

        }
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
        et_value.requestFocus()
    }

    private fun showBottomSheet() {
        requireActivity().hideKeyboard()
        tv_info_desc.text = getString(R.string.send_token_rewards)
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 600
        transition.addTarget(fl_bottom_sheet)
        transition.addListener(object : Transition.TransitionListener {
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
        transition.addListener(object : Transition.TransitionListener {
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

    fun hideTint() {
        ObjectAnimator.ofFloat(fl_tint, "alpha", 0f).apply {
            duration = 500
            start()
        }
    }

    override fun onGotitClicked() {
        //closeBottomSheet()
        viewModel._bottomSheetVisiblity.value = false

    }



}