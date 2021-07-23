package com.emcash.customerapp.ui.intro

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.animation.addListener
import androidx.fragment.app.activityViewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.isNougatOrAbove
import com.emcash.customerapp.extensions.obtainViewModel
import kotlinx.android.synthetic.main.fragment_first_intro.*
import kotlinx.android.synthetic.main.fragment_first_intro.btn_next
import kotlinx.android.synthetic.main.fragment_fourth_intro.*
import timber.log.Timber

class FourthIntroFragment : Fragment() {
    private val viewModel: IntroViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fourth_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performEnterTransition()

        btn_next.setOnClickListener {
            viewModel._screenPosition.value = 4
        }

    }

    private fun performEnterTransition() {
        val fade = ScaleAnimation(
            .8f,
            1f,
            .8f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        fade.duration = 500
        fade.fillAfter = false
        fade.isFillEnabled = false
        fade.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Timber.e("Anim end")
            }

            override fun onAnimationStart(p0: Animation?) {
                ObjectAnimator.ofFloat(iv_intro_cup, "alpha", 1f).apply {
                    duration = 500
                    start()
                }.addListener(onEnd = {
                    Timber.e("Anim end")
                    //  viewModel._screenPosition.value =2

                }

                )

            }
        })
        iv_intro_cup?.startAnimation(fade)


        val scale = ScaleAnimation(
            2f,
            1f,
            2f,
            1f,
            Animation.RELATIVE_TO_SELF,
            -1f,
            Animation.RELATIVE_TO_SELF,
            -1f
        )
        scale.duration = 600
        scale.fillAfter = false
        scale.isFillEnabled = false
        scale.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Timber.e("Anim end")
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })
        fl_toppings?.startAnimation(scale)

    }


}