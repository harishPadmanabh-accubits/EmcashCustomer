package com.emcash.customerapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.ChangeBounds
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.users
import com.emcash.customerapp.ui.history.TransactionHistory
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.ui.loademcash.LoadEmcashActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.qr.QrScannerActivity
import com.emcash.customerapp.ui.rewards.MyRewardsActivity
import com.emcash.customerapp.ui.settings.SettingsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LevelProfileImageView
import com.emcash.customerapp.utils.RC_CAMERA_PERM
import kotlinx.android.synthetic.main.activity_home.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class HomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
EasyPermissions.RationaleCallbacks{


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.sharedElementEnterTransition.duration = 500
        setupViews()


    }

    private fun setupViews() {
        cv_balance.setOnClickListener {
            openActivity(WalletActivity::class.java)
        }

        rv_recent_transactions.apply {
            //layoutManager = GridLayoutManager(this@HomeActivity, 5)
            adapter = RecentTransactionsAdapter(users)
        }

        tv_load_emcash.setOnClickListener {
            openLoadEmcash()
        }

        iv_user_image.setOnClickListener {
            openSettings()
        }

        tv_info_history.setOnClickListener {
            openHistory()
        }

        tv_info_rewards.setOnClickListener {
            openRewards()
        }

        iv_qr_scanner.setOnClickListener {
            openQRScanner()
        }

        fab_new_payment.setOnClickListener {
            openNewPayment()
        }

    }

    private fun openRewards() {
        openActivity(MyRewardsActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openHistory() {
        openActivity(TransactionHistory::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openSettings() {
        openActivity(SettingsActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openLoadEmcash() {
        openActivity(LoadEmcashActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openNewPayment(){
        openActivity(NewPaymentActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
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
            openActivity(QrScannerActivity::class.java)
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

//https://blog.mindorks.com/implementing-easy-permissions-in-android-android-tutorial