package com.emcash.customerapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.emcash.customerapp.DeepLinkFactory
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.contacts.ContactsGroup
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.history.TransactionHistory
import com.emcash.customerapp.ui.home.adapter.RecentTransactionAdapter
import com.emcash.customerapp.ui.loadEmcash.LoadEmcashActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import com.emcash.customerapp.ui.notifications.NotificationsActivity
import com.emcash.customerapp.ui.qr.QrScannerActivity
import com.emcash.customerapp.ui.rewards.MyRewardsActivity
import com.emcash.customerapp.ui.settings.SettingsActivity
import com.emcash.customerapp.ui.viewAllTransactions.ViewAllTransactionsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_switch_accout.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

import androidx.core.util.Pair
import android.view.View
import androidx.core.app.ActivityOptionsCompat


class HomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks, ContactsListener {
    private val loader by lazy {
        LoaderDialog(this)
    }

    private val deeplink by lazy {
        intent.getStringExtra(KEY_DEEPLINK).toString()
    }

    private val isFromDeeplink by lazy {
        intent.getBooleanExtra(IS_FROM_DEEPLINK, false)
    }

    private val viewModel: HomeViewModel by viewModels()

    private val recentTransactionsAdapter by lazy {
        RecentTransactionAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkDataFromPendingIntent()
        setContentView(R.layout.activity_home)
        window.sharedElementEnterTransition.duration = 500
        setupViews()
        lifecycleScope.launchWhenResumed {
            validateCache()
        }

        lifecycleScope.launchWhenResumed {
            getProfileDetailsFromServer()
        }

    }

    private fun checkDataFromPendingIntent() {
        if (deeplink.isNotBlank() && isFromDeeplink) {
            val intent = DeepLinkFactory.getIntentFromDeeplink(deeplink.toUri(), this)
            startActivity(intent)
        }
    }


    private fun renderRecentTransactions(data: List<RecentTransactionItem>) {
        if (data.isNotEmpty()) {
            Timber.e("RecentTransSize ${data.size}")
            recentTransactionsAdapter.submitList(data)
            iv_no_transactions.hide()
        } else {
            iv_no_transactions.show()
            rv_recent_transactions.hide()
            iv_no_transactions.setOnClickListener {
                openNewPayment()
            }
        }

    }

    private fun validateCache() {
        val profileCache = viewModel.syncManager.profileDetails
        if (profileCache != null)
            renderProfileDetails(profileCache)
    }

    private fun getProfileDetailsFromServer() {
        viewModel.profileDetails.observe(this, Observer {
            when (it.status) {
                ApiCallStatus.SUCCESS -> {
                    val profileData = it.data
                    if (profileData != null) {
                        renderProfileDetails(profileData)
                    }
                }
                ApiCallStatus.ERROR -> {
                    showShortToast(it.errorMessage)
                    hideLoader()
                }

            }
        })
    }

    private fun renderProfileDetails(profileDetails: ProfileDetailsResponse.Data) {
        tv_score_count.text = profileDetails.customer.score.toString()
        tv_level.text = profileDetails.customer.rewardLevel.toRewardLevelString(this)
        iv_user_image.loadImageWithErrorCallback(profileDetails.profileImage, onError = {
            tv_user_name_letter.text = profileDetails.name.first().toString()
        })
        coinProfileImageView.setImage(profileDetails.profileImage)
        tv_tv_balance.text = profileDetails.wallet.amount.toString()
        setLevelShower(profileDetails.customer.rewardLevel)
        setNotificationBadge(profileDetails.notificationCount)
        renderRecentTransactions(profileDetails.recentTransactions)
        hideLoader()
        cl_root.show()


    }

    private fun setNotificationBadge(notificationCount: Int) {
        if (notificationCount > 0) {
            tv_notification_count.text = notificationCount.toNotificationCount()
            cv_notification_count.show()
        } else {
            cv_notification_count.hide()
        }
    }

    private fun hideLoader() {
        if (loader.isShowing)
            loader.dismiss()
    }

    private fun setLevelShower(rewardLevel: Int) {
        when (rewardLevel) {
            1 -> iv_level_shower.loadImageWithResId(R.drawable.ic_level_green)
            2 -> iv_level_shower.loadImageWithResId(R.drawable.ic_level_yellow)
            3 -> iv_level_shower.loadImageWithResId(R.drawable.ic_level_red)
        }
    }

    private fun setupViews() {
        rv_recent_transactions.adapter = recentTransactionsAdapter

        iv_menu_handle.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                showSwitchAccountDialog()
            }
        })

        if (hasInternet()) {
            configureNavigation()
        } else {
            showShortToast(getString(R.string.internet_required))
        }
    }

    private fun configureNavigation() {
        cv_balance.setOnClickListener {
            openWalletScreen()
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

        iv_notifications.setOnClickListener {
            openActivity(NotificationsActivity::class.java)
            finish()
        }
    }

    private fun openWalletScreen() {
        openActivity(WalletActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            val options = ActivityOptions.makeSceneTransitionAnimation(this)
//            val intent = Intent(this, WalletActivity::class.java)
//            startActivity(intent, options.toBundle())
        finish()
    }

    private fun openRewards() {
        openActivity(MyRewardsActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()

    }

    private fun openHistory() {
        openActivity(TransactionHistory::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()

    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        val sharedDp = Pair.create<View?, String>(iv_user_image as View?, "dp")
        val sharedName = Pair.create<View?, String>(tv_user_name_letter as View?, "name")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedDp, sharedName)
        startActivity(intent, options.toBundle())
//        openActivity(SettingsActivity::class.java)
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//        finish()
    }

    private fun openLoadEmcash() {
        openActivity(LoadEmcashActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun openNewPayment() {
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }

    private fun hasContactPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)
    }

    fun openQRScanner() {
        if (hasCameraPermission()) {
            openActivity(QrScannerActivity::class.java) {
                this.putInt(LAUNCH_SOURCE, SCREEN_HOME)
            }
            finish()
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
        openQRScanner()
    }

    @SuppressLint("StringFormatMatches")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Timber.e("Camera Permission ${hasCameraPermission()}")
        }
    }


    override fun onSelectedFromRecentContacts(contact: RecentTransactionItem) {
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME_RECENT_CONTACTS)
            this.putInt(KEY_BEN_ID, contact.userId)
        }
    }

    override fun onSelectedFromAllContacts(contact: ContactsGroup.ContactInfo) {

    }

    override fun onSelectedViewAllTransactions() {
        openActivity(ViewAllTransactionsActivity::class.java)
        finish()
    }


    fun showSwitchAccountDialog() {
        val switchAccountDialog = Dialog(this)
        switchAccountDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_switch_accout)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()

            confirm_lay.setOnClickListener {
                applicationContext.logoutFromEmCash()
                finish()
            }

            cancel_lay.setOnClickListener {
                dismiss()
            }

        }

    }


}



