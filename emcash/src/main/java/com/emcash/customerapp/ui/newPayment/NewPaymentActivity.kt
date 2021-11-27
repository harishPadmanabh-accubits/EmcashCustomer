package com.emcash.customerapp.ui.newPayment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.EmCashListener
import com.emcash.customerapp.R
import com.emcash.customerapp.enums.TransactionType
import com.emcash.customerapp.extensions.fromJson
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.payments.QRResponse
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.ui.qr.QrScannerActivity
import com.emcash.customerapp.ui.viewAllTransactions.ViewAllTransactionsActivity
import com.emcash.customerapp.utils.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.lang.Exception

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class NewPaymentActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks, EmCashListener {

    val viewModel: NewPaymentViewModel by viewModels()

    val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, SCREEN_TRANSFER)
    }

    private val destination by lazy {
        intent.getIntExtra(LAUNCH_DESTINATION, 0)
    }

    val isFromQR by lazy {
        intent.getBooleanExtra(KEY_IS_FROM_QR, false)
    }

    val qrDataProfile by lazy {
        intent.getStringExtra(KEY_QR_DATA)?.fromJson(QRResponse.Data::class.java)
    }

    private val benId by lazy {
        intent.getIntExtra(KEY_BEN_ID, 0)
    }

    val isFromPinCancel by lazy {
        intent.getBooleanExtra(KEY_IS_FROM_CANCEL_PIN, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_payment)

        if (benId > 0)
            viewModel.beneficiaryId = benId

        if (source == SCREEN_HOME_RECENT_CONTACTS)
            viewModel.gotoScreen(CHAT)
        handleDestinations(destination)
        observe()

    }

    private fun handleDestinations(destination: Int) {
        if (destination > 0) {
            when (destination) {
                SCREEN_RECEIPT -> viewModel.gotoScreen(RECEIPT)
                SCREEN_TRANSFER -> {
                    if (isFromQR) {
                        qrDataProfile?.let { profile ->
                            val contactBundle = bundleOf(
                                KEY_SELECTED_CONTACT to profile.id,
                                KEY_TRANSACTION_TYPE to TYPE_TRANSFER
                            )
                            viewModel.gotoScreen(TRANSFER, contactBundle)
                        }
                    }else{
                        viewModel.gotoScreen(TRANSFER)
                    }
                }
                SCREEN_CHAT -> {
                    viewModel.gotoScreen(CHAT)
                }

            }
        }

    }

    private fun observe() {
        viewModel.apply {
            screenConfig.observe(this@NewPaymentActivity, Observer { screenConfig ->
                when (screenConfig.screen) {
                    CONTACTS -> openContactsScreen(screenConfig.bundle)
                    TRANSFER -> openTransferScreen(screenConfig.bundle)
                    CHAT -> openPaymentChatScreen(screenConfig.bundle)
                    RECEIPT -> openPaymentReceipt(screenConfig.bundle)
                    PIN -> openPinScreen(screenConfig.bundle)
                    SCAN -> openQRScanner(screenConfig.bundle)
                }
            })
        }
    }

    private fun openPinScreen(bundle: Bundle?) {
        try {
            val type =bundle?.get(KEY_TRANSACTION_TYPE) as TransactionType
            EmCashCommunicationHelper.getParentListener().onVerifyPin(type)
        }catch (e:Exception){
            e.printStackTrace()
            showShortToast(getString(R.string.internal_error))
        }
    }

    private fun openTransferScreen(bundle: Bundle?) {
        supportFragmentManager.commit {
            addToBackStack("Transfer Screen")
            this.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            replace<TransferFragment>(R.id.container, "Transfer to", bundle)
        }
    }

    private fun openContactsScreen(bundle: Bundle?) {
        supportFragmentManager.commit {
            this.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace<ContactsFragment>(R.id.container)
        }
    }


    private fun openPaymentChatScreen(bundle: Bundle?) {
        supportFragmentManager.commit {
            addToBackStack("Chat Screen")
            this.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace<PaymentChatFragment>(R.id.container, "Chats", bundle)
        }
    }

    private fun openPaymentReceipt(bundle: Bundle?) {
        supportFragmentManager.commit {
            addToBackStack("Receipt Screen")
            this.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace<PaymentReceiptFragment>(R.id.container)
        }
    }

    override fun onBackPressed() {
        if (viewModel._bottomSheetVisiblity.value == true)
            viewModel._bottomSheetVisiblity.value = false
        else {
            when (viewModel.screenConfig.value?.screen) {
                RECEIPT -> {
                    viewModel.gotoScreen(CHAT)
                }
                CONTACTS -> {
                    openActivity(HomeActivity::class.java)
                    finish()
                }
                CHAT -> {
                    when (source) {
                        SCREEN_HOME -> viewModel.gotoScreen(CONTACTS)
                        SCREEN_VIEW_ALL -> {
                            openActivity(ViewAllTransactionsActivity::class.java)
                            finish()
                        }
                        else -> {
                            openActivity(HomeActivity::class.java)
                            finish()
                        }
                    }

                }
                PIN -> {
                    viewModel.gotoScreen(TRANSFER)
                }
                TRANSFER -> {
                    viewModel.clearTransferScreenCache()
                    viewModel.gotoScreen(CHAT)
                }
                else -> {
                    openActivity(HomeActivity::class.java)
                    finish()
                }
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }

    private fun hasContactPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)
    }

    private fun openQRScanner(bundle: Bundle?) {
        if (hasCameraPermission()) {
            openActivity(QrScannerActivity::class.java) {
                this.putInt(LAUNCH_SOURCE, SCREEN_TRANSFER)
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Timber.e("onRationaleAccepted:%s", requestCode)

    }

    override fun onRationaleDenied(requestCode: Int) {
        Timber.e("onRationaleDenied:%s", requestCode)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        openActivity(QrScannerActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_TRANSFER)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

    @SuppressLint("StringFormatMatches")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Timber.e("Camera Permission ${hasCameraPermission()}")
        }
    }

    override fun onLoginStatusCallback(status: Boolean) {
    }


}