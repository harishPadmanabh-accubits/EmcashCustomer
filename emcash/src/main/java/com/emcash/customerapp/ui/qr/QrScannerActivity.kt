package com.emcash.customerapp.ui.qr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.emcash.customerapp.R
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens
import com.emcash.customerapp.ui.newPayment.NewPaymentViewModel
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.SCREEN_TRANSFER
import kotlinx.android.synthetic.main.activity_qr_scanner.*

class QrScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    private val viewModel:NewPaymentViewModel by viewModels()

    val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, SCREEN_TRANSFER)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)
        codeScanner = CodeScanner(this, scanner_view)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                viewModel.gotoScreen(NewPaymentScreens.TRANSFER)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }

    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onBackPressed() {
        viewModel.gotoScreen(NewPaymentScreens.CONTACTS)
        super.onBackPressed()
    }
}