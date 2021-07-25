package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.ui.qr.QrScannerActivity
import timber.log.Timber

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class NewPaymentActivity : AppCompatActivity() {

    val viewModel:NewPaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_payment)
        observe()

    }

    private fun observe() {
        viewModel.apply {
            screens.observe(this@NewPaymentActivity, Observer {screen->
                when(screen){
                    CONTACTS->openContactsScreen()
                    TRANSFER->openTransferScreen()
                    CHAT->openPaymentChatScreen()
                    RECEIPT->openPaymentReceipt()
                    PIN->openPinScreen()
                }
            })
        }
    }

    private fun openPinScreen() {
        supportFragmentManager.commit {
            addToBackStack("Pin Screen")
            replace<EmcashPinFragment>(R.id.container)
        }
    }

    fun openTransferScreen(){
        supportFragmentManager.commit {
     //       addToBackStack("Transfer Screen")
            replace<TransferFragment>(R.id.container)
        }
    }

    fun openContactsScreen(){
        supportFragmentManager.commit {
            addToBackStack("Contacts Screen")
            replace<ContactsFragment>(R.id.container)
        }
    }

    fun openQRScanner(){
        openActivity(QrScannerActivity::class.java)
    }

    fun openPaymentChatScreen(){
        supportFragmentManager.commit {
            addToBackStack("Chat Screen")
            replace<PaymentChatFragment>(R.id.container)
        }
    }

    fun openPaymentReceipt(){
        supportFragmentManager.commit {
            addToBackStack("Receipt Screen")
            replace<PaymentReceiptFragment>(R.id.container)
        }
    }
}