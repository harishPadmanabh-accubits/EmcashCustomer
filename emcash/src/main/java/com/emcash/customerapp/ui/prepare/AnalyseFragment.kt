package com.emcash.customerapp.ui.prepare

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import kotlinx.android.synthetic.main.fragment_first_intro.*
import kotlinx.android.synthetic.main.frame_analyse_score.*
import timber.log.Timber

class AnalyseFragment:Fragment(R.layout.frame_analyse_score) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    fun openPreparedFragment(){
        requireActivity().  supportFragmentManager.commit {
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
            setReorderingAllowed(true)
           // this.addSharedElement(iv_curve,"curve")
            replace<PreparedFragment>(R.id.container)
        }
    }
}