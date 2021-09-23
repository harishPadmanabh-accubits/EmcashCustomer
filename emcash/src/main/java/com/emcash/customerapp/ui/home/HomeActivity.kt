package com.emcash.customerapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.emcash.customerapp.DeepLinkFactory
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.DummyContactsRawData
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.contacts.ContactsGroup
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.ui.history.TransactionHistory
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.ui.loademcash.LoadEmcashActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import com.emcash.customerapp.ui.notifiacations.NotificationsActivity
import com.emcash.customerapp.ui.qr.QrScannerActivity
import com.emcash.customerapp.ui.rewards.MyRewardsActivity
import com.emcash.customerapp.ui.settings.SettingsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class HomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks, ContactsListener {

    private val profileDataCache by lazy {
        intent.getStringExtra(KEY_PROFILE_DATA_CACHE)
    }

    private val loader by lazy {
        LoaderDialog(this)
    }

    private val deeplink by lazy {
        intent.getStringExtra(KEY_DEEPLINK).toString()
    }

    private val type by lazy {
        intent.getStringExtra(KEY_TYPE)
    }

    private val isFromDeeplink by lazy {
        intent.getBooleanExtra(IS_FROM_DEEPLINK,false)
    }

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.sharedElementEnterTransition.duration = 500
       checkDataFromPendingIntent()

        lifecycleScope.launch (Dispatchers.Main){
           // validateCache(profileDataCache)
            getProfileDetailsFromServer()
        }
        lifecycleScope.launch (Dispatchers.Main){
            getRecentTransactions()
        }


        setupViews()
    }

    private fun checkDataFromPendingIntent() {
        if(deeplink.isNotBlank() && isFromDeeplink){
            val intent = DeepLinkFactory.getIntentFromDeeplink(deeplink.toUri(),this)
            startActivity(intent)
        }
    }

    private fun getRecentTransactions() {
        viewModel.recentTransactions.observe(this, Observer {
            when(it.status){
                ApiCallStatus.SUCCESS->{
                    renderRecentTransactions(it.data)
                }
            }
        })

    }

    private fun renderRecentTransactions(data: RecentTransactionResponse.Data?) {
        data?.let {
            if(it.transactionList.isNotEmpty()){
                rv_recent_transactions.apply {
                    adapter = RecentTransactionsAdapter(it.transactionList, this@HomeActivity)
                }
                frame_recent_transactions.show()
                iv_no_transactions.hide()
            }else{
                frame_recent_transactions.show()
                iv_no_transactions.show()
                rv_recent_transactions.hide()
            }
        }
    }

    private fun validateCache(profileDataCache: String?) {
        if (!profileDataCache.isNullOrEmpty()) {    //from intent
            Timber.e("profile intent ")
            val profileDetails = profileDataCache.fromJson(ProfileDetailsResponse.Data::class.java)
            renderProfileDetails(profileDetails)
        } else {                //get from cache - if null - get from server
            val profileCache = viewModel.syncManager.profileDetails
            if (profileCache != null)
                renderProfileDetails(profileCache)
            else
                getProfileDetailsFromServer()
        }

    }

    private fun getProfileDetailsFromServer() {
        viewModel.profileDetails.observe(this, Observer {
            when (it.status) {
                ApiCallStatus.SUCCESS -> {
                    Timber.e("Observer Success")
                    val profileData = it.data
                    if (profileData != null)
                        renderProfileDetails(profileData)
                }
                ApiCallStatus.ERROR ->{
                    showShortToast(it.errorMessage)
                    hideLoader()
                }
                ApiCallStatus.LOADING->{
                    loader.show()
                }
            }
        })
    }

    private fun renderProfileDetails(profileDetails: ProfileDetailsResponse.Data) {
        tv_score_count.text = profileDetails.customer.score.toString()
        tv_level.text = profileDetails.customer.rewardLevel.toRewardLevelString(this)
        iv_user_image.loadImageWithPlaceHolder(
            profileDetails.profileImage,
            R.drawable.ic_profile_placeholder
        )
        coinProfileImageView.setImage(profileDetails.profileImage)
        tv_tv_balance.text = profileDetails.wallet.amount.toString()
        setLevelShower(profileDetails.customer.rewardLevel)
        hideLoader()
        cl_root.show()


    }

    private fun hideLoader() {
        if(loader.isShowing)
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
        cv_balance.setOnClickListener {
            openActivity(WalletActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
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
        }

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
        openActivity(SettingsActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openLoadEmcash() {
        openActivity(LoadEmcashActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME)
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openNewPayment() {
        openActivity(NewPaymentActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
            openActivity(QrScannerActivity::class.java){
                this.putInt(LAUNCH_SOURCE, SCREEN_HOME)
            }
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

    override fun onContactSelected(contact: DummyContactsRawData?, recentContact: DummyUserData?) {
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME_RECENT_CONTACTS)
        }
    }

    override fun onSelectedFromRecentContacts(contact: RecentTransactionItem) {
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME_RECENT_CONTACTS)
            this.putInt(KEY_BEN_ID,contact.userId)
        }
    }

    override fun onSelectedFromAllContacts(contact: ContactsGroup.ContactInfo) {
    }


}

enum class RewardLevels {
    LOW, MEDIUM, HIGH
}

//https://blog.mindorks.com/implementing-easy-permissions-in-android-android-tutorial