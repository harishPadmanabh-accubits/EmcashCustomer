package com.emcash.customerapp.ui.prepare

import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import kotlinx.android.synthetic.main.activity_prepare_em_cash.*
import kotlinx.android.synthetic.main.bottom_sheet_how_it_works.*
import timber.log.Timber


class PrepareEmCashActivity : AppCompatActivity(),BottomSheetListener {

    private val viewModel: PrepareEmcashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_em_cash)
        observe()
        openAnalyseFragment()
        setupBottomSheet()

    }

    private fun setupBottomSheet() {
        btn_got_it.setOnClickListener {
            onGotitClicked()
        }
    }

    private fun observe() {
        viewModel.apply {
            bottomSheetVisibility.observe(this@PrepareEmCashActivity, Observer {
                when (it) {
                    true -> showBottomSheet()
                    false -> closeBottomSheet()
                }
            })


        }
    }



    fun openAnalyseFragment() {
        supportFragmentManager.commit {
            //this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<AnalyseFragment>(R.id.container)
        }
    }

    fun openPreparedFragment() {
        supportFragmentManager.commit {
            this.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<PreparedFragment>(R.id.container)
        }
    }

    fun animateCurve(onEnd: () -> Unit) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val pushDown = TranslateAnimation(
            0F, screenWidth.toFloat() / 4, 0F, screenHeight.toFloat() / 2
        )
        pushDown.duration = 750
        pushDown.fillAfter = true
        pushDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {

            }

            override fun onAnimationStart(p0: Animation?) {
                ObjectAnimator.ofFloat(iv_curve, "scaleX", -1f).apply {
                    duration = 1000
                    start()
                }.addListener(onEnd = {
                    Timber.e("Anim end")

                }

                )
                ObjectAnimator.ofFloat(iv_curve, "translationX", screenWidth.toFloat() / 3).apply {
                    duration = 800
                    start()
                }.addListener(onEnd = {
                    Timber.e("Anim end")
                    iv_curve.hide()
                    onEnd.invoke()
                }
                )
            }
        })
        iv_curve.startAnimation(pushDown)
    }

    private fun showBottomSheet() {
        val transition: Transition = Slide(Gravity.BOTTOM);
        transition.duration = 600
        transition.addTarget(fl_bottom_sheet)
        transition.addListener(object : Transition.TransitionListener{
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

    }

    private fun closeBottomSheet() {
        val transition: Transition = Slide(Gravity.BOTTOM);
        transition.duration = 500
        transition.addTarget(fl_bottom_sheet)
        transition.addListener(object : Transition.TransitionListener{
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

    override fun onBackPressed() {
        if (viewModel._bottomSheetVisiblity.value == true)
            viewModel._bottomSheetVisiblity.value = false
        else
            super.onBackPressed()
    }

    fun showTint() {
        ObjectAnimator.ofFloat(fl_tint, "alpha", 90f).apply {
            duration = 500
            start()
        }
    }

    fun hideTint(){
        ObjectAnimator.ofFloat(fl_tint, "alpha", 0f).apply {
            duration = 500
            start()
        }
    }

    override fun onGotitClicked() {
        closeBottomSheet()
    }
}

interface BottomSheetListener{
    fun onGotitClicked()
}