package com.emcash.customerapp.ui.prepare

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.core.animation.addListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.intro.FirstIntroFragment
import kotlinx.android.synthetic.main.activity_prepare_em_cash.*

import timber.log.Timber

class PrepareEmCashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_em_cash)
        openAnalyseFragment()

    }

    fun openAnalyseFragment(){
        supportFragmentManager.commit {
            //this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<AnalyseFragment>(R.id.container)
        }
    }

    fun openPreparedFragment(){
        supportFragmentManager.commit {
            this.setCustomAnimations(android.R.anim.fade_out,android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<PreparedFragment>(R.id.container)
        }
    }

    fun animateCurve(onEnd:()->Unit){
        val pushDown = TranslateAnimation(
            0F, 500F, 0F, 1300F
        )
        pushDown.duration = 1000
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
                ObjectAnimator.ofFloat(iv_curve, "translationX", 400f).apply {
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


}