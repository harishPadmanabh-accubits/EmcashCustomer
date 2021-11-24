package com.emcash.customerapp.ui.newPayment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.enums.TransactionType
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.payments.TransactionGroupResponse
import com.emcash.customerapp.model.payments.TransactionHistoryResponse
import com.emcash.customerapp.ui.newPayment.adapters.PaymentChatListAdapter
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.block_layout.*
import kotlinx.android.synthetic.main.payment_chats.*
import kotlinx.android.synthetic.main.payment_chats.iv_user_dp
import kotlinx.android.synthetic.main.payment_chats.tv_name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PaymentChatFragment : Fragment(R.layout.payment_chats), PaymentHistoryItemClickListener {

    val viewModel: NewPaymentViewModel by activityViewModels()
    private var isBlockedLoggedInUser: Boolean = false
    private var isBlockedContactUser: Boolean = false
    private val chatAdapter by lazy {
        PaymentChatListAdapter(this)
    }

    val loader by lazy { LoaderDialog(requireContext()) }

    var bundle = arguments


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_pay.setOnClickListener {
            gotoTransferScreen()
        }
        btn_request.setOnClickListener {
            gotoRequestScreen()
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        refresh()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getHistory().observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ApiCallStatus.LOADING -> loader.showLoader()
                    ApiCallStatus.SUCCESS -> {
                        loader.hideLoader()
                        renderDetails(it.data)
                    }
                    ApiCallStatus.ERROR -> {
                        loader.hideLoader()
                        requireActivity().showShortToast(it.errorMessage)
                    }
                }
            })
        }

        viewModel.pagedHistoryItems.observe(viewLifecycleOwner, Observer {
            chatAdapter.submitData(lifecycle, it)
        })

    }

    private fun gotoRequestScreen() {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to viewModel.beneficiaryId.toString(),
            KEY_TRANSACTION_TYPE to TYPE_REQUEST
        )
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER, bundle)
    }

    private fun gotoTransferScreen() {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to viewModel.beneficiaryId.toString(),
            KEY_TRANSACTION_TYPE to TYPE_TRANSFER
        )
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER, bundle)
    }

    private fun renderDetails(data: TransactionHistoryResponse.Data?) {
        data?.let {
            val beneficiary = it.contact
            val wallet = it.wallet
            fl_user_level.setlevel(beneficiary.level)
            iv_user_dp.loadImageWithErrorCallback(beneficiary.profileImage, onError = {
                tv_user_name_letter.apply {
                    text = beneficiary.name.first().toString().toUpperCase(Locale.ROOT)
                    show()
                }
            })
            tv_name.text = beneficiary.name
            tv_contact_number.text = beneficiary.phoneNumber
            iv_user_coin_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)
            tv_value_balance.text = wallet.amount.toString()
            cl_root.show()
            isBlockedContactUser = it.contact?.isLoggedInUserBlockedContactUser
            isBlockedLoggedInUser = it.contact.isContactUserBlockedLoggedInUser

            handleBlocking(isBlockedLoggedInUser, isBlockedContactUser)

            iv_menu.setOnClickListener {
                showOptionsmenu(
                    it,
                    viewModel.beneficiaryId,
                    beneficiary.name,
                    beneficiary.phoneNumber,
                    beneficiary.profileImage,
                    beneficiary.level
                )
            }


        }
    }

    private fun handleBlocking(blockedLoggedInUser: Boolean, blockedContactUser: Boolean) {
        if (!blockedContactUser && !blockedLoggedInUser) {
            btn_request.show()
            btn_pay.show()
        } else {
            btn_request.hide()
            btn_pay.hide()
        }

    }

    override fun onInfoCardClicked(transactionId: String) {
        viewModel.syncManager.initiatedRefId = transactionId
        viewModel.gotoScreen(NewPaymentScreens.RECEIPT)
    }

    override fun onAcceptPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction) {
        if(isBlockedContactUser || isBlockedLoggedInUser)
            requireActivity().showShortToast(getString(R.string.block_success))
        else{
            viewModel.syncManager.initiatedRefId = transaction.id
            val bundle = bundleOf(KEY_TRANSACTION_TYPE to TransactionType.ACCEPT)
            viewModel.gotoScreen(NewPaymentScreens.PIN, bundle)
        }
    }

    override fun onRejectPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction) {
        if(isBlockedContactUser || isBlockedLoggedInUser)
            requireActivity().showShortToast(getString(R.string.block_success))
        else {
            viewModel.syncManager.initiatedRefId = transaction.id
            val bundle = bundleOf(KEY_TRANSACTION_TYPE to TransactionType.REJECT)
            viewModel.gotoScreen(NewPaymentScreens.PIN, bundle)
        }
    }

    private fun showOptionsmenu(
        view: View,
        userId: Int,
        name: String,
        phoneNumber: String,
        profileImage: String,
        level: Int
    ) {
        val menu = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            PopupMenu(context, view, Gravity.END, 40, R.style.DropDown)
        } else {
            PopupMenu(context, view)
        }
        menu.inflate(R.menu.chat_options_menu)
        val menuOpts = menu.menu


        menuOpts.getItem(1).title =
            if (isBlockedContactUser) getString(R.string.unblock_account) else getString(R.string.block_account)


        menu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.handle_blocking -> {
                    if (item.title.equals(getString(R.string.block_account)))
                        showBlockDialog(userId, name, phoneNumber, profileImage, level)
                    else
                        unblockAccount(userId)

                }
                R.id.refresh -> {
                    refresh()
                }

            }

            true
        })

        menu.show()
    }

    private fun unblockAccount(userId: Int) {
        loader.showLoader()
        viewModel.unblockAccount(userId){status, error ->
            when(status){
                true->{
                    loader.hideLoader()
                    requireActivity().showShortToast(getString(R.string.unblock_success))
                    isBlockedContactUser = false
                    handleBlocking(isBlockedLoggedInUser,isBlockedContactUser)
                }
                false->{
                    loader.hideLoader()
                    requireActivity().showShortToast(error)

                }
            }
        }
    }

    private fun showBlockDialog(
        userId: Int,
        name: String,
        phoneNumber: String,
        profileImage: String,
        level: Int
    ) {
        val dialog = Dialog(requireActivity())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.block_layout)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            tv_name.text = name
            tv_number.text = phoneNumber
            iv_user_dp.loadImageWithErrorCallback(
                profileImage,
                onError = {
                    tv_user_name_letter_block.text = name.first().toString().toUpperCase(Locale.ROOT)
                }
            )
            fll_holder.setlevel(level)
            tv_block.text = getString(R.string.block)
            cancel_lay.setOnClickListener {
                dialog.dismiss()
            }
            confirm_lay.setOnClickListener {
                dialog.dismiss()
                blockAccount(userId)

            }
        }.show()
    }

    private fun blockAccount(userId: Int) {
        loader.showLoader()
        viewModel.blockAccountAsync(userId){ status, error ->
            when(status){
                true->{
                    loader.hideLoader()
                    requireActivity().showShortToast(getString(R.string.block_success))
                    isBlockedContactUser = true
                    handleBlocking(isBlockedLoggedInUser,isBlockedContactUser)
                }
                false->{
                    loader.hideLoader()
                    requireActivity().showShortToast(error)
                }
            }
        }
    }


    private fun refresh() {
        rv_history.apply {
            setHasFixedSize(true)
            adapter = chatAdapter
        }
        viewModel._refreshChat.value = true
    }

    override fun onDestroyView() {
        rv_history.adapter = null
        super.onDestroyView()
    }


}

interface PaymentHistoryItemClickListener {
    fun onInfoCardClicked(transactionId: String)
    fun onAcceptPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction)
    fun onRejectPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction)
}