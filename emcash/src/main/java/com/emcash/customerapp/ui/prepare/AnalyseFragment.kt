package com.emcash.customerapp.ui.prepare

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.emcash.customerapp.R


class AnalyseFragment:Fragment(R.layout.frame_analyse_score) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({

            (requireActivity() as PrepareEmCashActivity).animateCurve(onEnd = {
                openPreparedFragment()
            })

        }, 1000)


    }


    fun openPreparedFragment(){
        requireActivity().  supportFragmentManager.commit {
       //     this.setCustomAnimations(android.R.anim.fade_out,android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<PreparedFragment>(R.id.container)
        }
    }
}