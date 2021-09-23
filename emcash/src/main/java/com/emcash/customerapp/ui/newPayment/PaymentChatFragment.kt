package com.emcash.customerapp.ui.newPayment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
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
import com.emcash.customerapp.model.BlockType
import com.emcash.customerapp.model.payments.TransactionGroupResponse
import com.emcash.customerapp.model.payments.TransactionHistoryResponse
import com.emcash.customerapp.ui.newPayment.adapters.PaymentChatListAdapter
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.block_layout.*
import kotlinx.android.synthetic.main.payment_chats.*
import kotlinx.android.synthetic.main.payment_chats.iv_user_dp
import kotlinx.android.synthetic.main.payment_chats.tv_name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PaymentChatFragment:Fragment(R.layout.payment_chats),PaymentHistoryItemClickListener {

    val viewModel : NewPaymentViewModel by activityViewModels()
    private var isBlockedLoggedInUser: Boolean = false
    private var isBlockedContactUser: Boolean = false
    val chatAdapter by lazy {
        PaymentChatListAdapter(this)
    }

    val loader by lazy{LoaderDialog(requireContext())}

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

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
            viewModel.getHistory().observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ApiCallStatus.LOADING-> loader.showLoader()
                    ApiCallStatus.SUCCESS->{
                        loader.hideLoader()
                        renderDetails(it.data)
                    }
                    ApiCallStatus.ERROR->{
                        loader.hideLoader()
                        requireActivity().showShortToast(it.errorMessage)
                    }
                }
            })
        }

        viewModel.pagedHistoryItems.observe(viewLifecycleOwner, Observer {
            chatAdapter.submitData(lifecycle,it)
        })

    }

    private fun gotoRequestScreen() {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to viewModel.beneficiaryId.toString(),
            KEY_TRANSACTION_TYPE to TYPE_REQUEST
        )
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER,bundle)
    }

    private fun gotoTransferScreen() {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to viewModel.beneficiaryId.toString(),
            KEY_TRANSACTION_TYPE to TYPE_TRANSFER
        )
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER,bundle)
    }

    private fun renderDetails(data: TransactionHistoryResponse.Data?) {
        data?.let {
            val beneficiary= it.contact
            val wallet = it.wallet
            fl_user_level.setlevel(beneficiary.level)
            iv_user_dp.loadImageWithPlaceHolder(beneficiary.profileImage,R.drawable.ic_profile_placeholder)
            tv_name.text = beneficiary.name
            tv_contact_number.text = beneficiary.phoneNumber
            iv_user_coin_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)
            tv_value_balance.text = wallet.amount.toString()
            cl_root.show()
            isBlockedContactUser = it.contact?.isLoggedInUserBlockedContactUser
            isBlockedLoggedInUser = it.contact.isContactUserBlockedLoggedInUser

            handleblocking(isBlockedLoggedInUser,isBlockedContactUser)

            iv_menu.setOnClickListener {
                showPopup(it,viewModel.beneficiaryId,beneficiary.name, beneficiary.phoneNumber,beneficiary.profileImage,beneficiary.level)
            }


        }
    }

    private fun handleblocking(blockedLoggedInUser: Boolean, blockedContactUser: Boolean) {
        if(!blockedContactUser && !blockedLoggedInUser){

            btn_request.show()
            btn_pay.show()
        }else{
            btn_request.hide()
            btn_pay.hide()
        }

    }

    override fun onItemClick(transactionId: String) {
        viewModel.syncManager.initiatedRefId = transactionId
        viewModel.gotoScreen(NewPaymentScreens.RECEIPT)
    }

    override fun onAcceptPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction) {
        viewModel.syncManager.initiatedRefId = transaction.id
        val bundle = bundleOf(KEY_TRANSACTION_TYPE to TransactionType.ACCEPT)
        viewModel.gotoScreen(NewPaymentScreens.PIN,bundle)

    }

    override fun onRejectPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction) {
        viewModel.syncManager.initiatedRefId = transaction.id
        val bundle = bundleOf(KEY_TRANSACTION_TYPE to TransactionType.REJECT)
        viewModel.gotoScreen(NewPaymentScreens.PIN,bundle)
    }

    private fun showPopup(
        view: View,
        userId: Int,
        name: String,
        phoneNumber: String,
        profileImage: String,
        level: Int
    ) {
        var popup = PopupMenu(requireContext(), view)
        popup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            PopupMenu(context, view, Gravity.END, 40, R.style.MyPopupMenu)
        } else {
            PopupMenu(context, view)
        }
        popup.inflate(R.menu.chat_options_menu)
        val menuOpts = popup.menu

        if (isBlockedContactUser == false) {
            menuOpts.getItem(2).title = "Block Account"


        } else {

            menuOpts.getItem(2).title = "Unblock Account"

        }


        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.help_support -> {
                    //help&support
                }
                R.id.block_account -> {
                    //block
                    dialogBlockUnBlock(userId,name,phoneNumber,profileImage,level)


                }
                R.id.refresh -> {
                   refresh()
                }

            }

            true
        })

        popup.show()
    }

    private fun dialogBlockUnBlock(
        userId: Int,
        name: String,
        phoneNumber: String,
        profileImage: String,
        level: Int
    ) {
      val  dialogBlockUnBlock = Dialog(requireActivity())
        dialogBlockUnBlock.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogBlockUnBlock.setContentView(R.layout.block_layout)
        dialogBlockUnBlock.setCancelable(true)
        dialogBlockUnBlock.setCanceledOnTouchOutside(true)
        dialogBlockUnBlock.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBlockUnBlock.show()

        dialogBlockUnBlock.tv_name.text = name
        dialogBlockUnBlock.tv_number.text = phoneNumber
        dialogBlockUnBlock.iv_user_dp.loadImageWithPlaceHolder(profileImage,R.drawable.ic_profile_placeholder)
        dialogBlockUnBlock.fll_holder.setlevel(level)

        if (isBlockedContactUser == false) {
            dialogBlockUnBlock.tv_block.text = "Block"

        } else {
            dialogBlockUnBlock.tv_block.text = "Unblock"
        }

        dialogBlockUnBlock.cancel_lay.setOnClickListener {
            dialogBlockUnBlock.dismiss()
        }
        dialogBlockUnBlock.confirm_lay.setOnClickListener {
            dialogBlockUnBlock.dismiss()
            if (isBlockedContactUser == false) {
                viewModel.blockContact(userId).observe(viewLifecycleOwner, Observer {
                    when(it.status){
                        ApiCallStatus.LOADING->loader.showLoader()
                        ApiCallStatus.SUCCESS->{
                            loader.hideLoader()
                            requireActivity().showShortToast("This user has been blocked.")
                            btn_request.hide()
                            btn_pay.hide()
                            isBlockedContactUser = true
                        }
                        ApiCallStatus.ERROR->{
                            loader.hideLoader()
                            requireActivity().showShortToast("Something Went wrong")

                        }
                    }
                })

            } else {
                viewModel.unblockContact(userId).observe(viewLifecycleOwner, Observer {
                    when(it.status){
                        ApiCallStatus.LOADING->loader.showLoader()
                        ApiCallStatus.SUCCESS->{
                            loader.hideLoader()
                            requireActivity().showShortToast("This user has been unblocked.")
                            btn_request.show()
                            btn_pay.show()
                            isBlockedContactUser = false

                        }
                        ApiCallStatus.ERROR->{
                            loader.hideLoader()
                            requireActivity().showShortToast("Something Went wrong")

                        }
                    }
                })

            }

        }
    }


    private fun refresh(){
        rv_history.apply {
            setHasFixedSize(true)
            adapter = chatAdapter

        }
        viewModel._refreshChat.value = true
    }




}

interface PaymentHistoryItemClickListener{
   fun onItemClick(transactionId:String)
   fun onAcceptPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction)
   fun onRejectPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction)
}