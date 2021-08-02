package com.emcash.customerapp.ui.newPayment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.ui.qr.QrScannerActivity
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.RC_CAMERA_PERM
import com.emcash.customerapp.utils.SCREEN_HOME_RECENT_CONTACTS
import com.emcash.customerapp.utils.SCREEN_TRANSFER
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class NewPaymentActivity : AppCompatActivity() , EasyPermissions.PermissionCallbacks,
EasyPermissions.RationaleCallbacks{

    val viewModel:NewPaymentViewModel by viewModels()

    val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, SCREEN_TRANSFER)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_payment)
        if(source == SCREEN_HOME_RECENT_CONTACTS)
            viewModel.gotoScreen(CHAT)
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
                    SCAN->openQRScanner()
                }
            })
        }
    }

    private fun openPinScreen() {
        supportFragmentManager.commit {
            addToBackStack("Pin Screen")
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
            replace<EmcashPinFragment>(R.id.container)
        }
    }

    fun openTransferScreen(){
        supportFragmentManager.commit {
            addToBackStack("Transfer Screen")
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
            replace<TransferFragment>(R.id.container)
        }
    }

    fun openContactsScreen(){
        supportFragmentManager.commit {
           // addToBackStack("Contacts Screen")
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
            replace<ContactsFragment>(R.id.container)
        }
    }


    fun openPaymentChatScreen(){
        supportFragmentManager.commit {
            addToBackStack("Chat Screen")
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
            replace<PaymentChatFragment>(R.id.container)
        }
    }

    fun openPaymentReceipt(){
        supportFragmentManager.commit {
            addToBackStack("Receipt Screen")
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
            replace<PaymentReceiptFragment>(R.id.container)
        }
    }

    override fun onBackPressed() {
        if (viewModel._bottomSheetVisiblity.value == true)
            viewModel._bottomSheetVisiblity.value = false
        else {
            when (viewModel.screens.value) {
                RECEIPT -> {
                    viewModel.gotoScreen(CHAT)
                }
                CONTACTS -> {
                    openActivity(HomeActivity::class.java)
                    finish()
                }
                CHAT -> {
                    openActivity(HomeActivity::class.java)
                    finish()
                }
                PIN -> {
                    viewModel.gotoScreen(TRANSFER)
                }
                TRANSFER -> {
                    viewModel.gotoScreen(CONTACTS)
                }
                else -> {
                    super.onBackPressed()
                }
            }
        }
    }
    private fun hasCameraPermission():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }

    private fun hasContactPermission():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)
    }

    fun openQRScanner() {
        if (hasCameraPermission())
        {
            openActivity(QrScannerActivity::class.java){
                this.putInt(LAUNCH_SOURCE, SCREEN_TRANSFER)
            }
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)

        }
        else
        {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode:Int,
                                            permissions:Array<String>,
                                            grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRationaleAccepted(requestCode:Int) {
        Timber.e("onRationaleAccepted:%s", requestCode)

    }
    override fun onRationaleDenied(requestCode:Int) {
        Timber.e("onRationaleDenied:%s", requestCode)
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
        openActivity(QrScannerActivity::class.java){
            this.putInt(LAUNCH_SOURCE, SCREEN_TRANSFER)
        }
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)

    }

    @SuppressLint("StringFormatMatches")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        {
            Timber.e("Camera Permission ${hasCameraPermission()}")
        }
    }


}