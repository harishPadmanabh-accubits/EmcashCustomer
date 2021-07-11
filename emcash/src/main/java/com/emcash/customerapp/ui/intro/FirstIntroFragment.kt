package com.emcash.customerapp.ui.intro

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.isNougatOrAbove
import com.emcash.customerapp.extensions.obtainViewModel
import kotlinx.android.synthetic.main.fragment_first_intro.*
import timber.log.Timber


class FirstIntroFragment : Fragment() {

    private lateinit var viewModel: IntroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        btn_next.setOnClickListener {
                performExitTransitionAndNavigate(iv_intro_art_man)

        }
    }

    private fun performExitTransitionAndNavigate(ivInroImage: ImageView?) {
        val fade_in = ScaleAnimation(
            1f,
            0.5f,
            1f,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        fade_in.duration = 500
        fade_in.fillAfter = false
        fade_in.isFillEnabled = false
        fade_in.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Timber.e("Anim end")
            }

            override fun onAnimationStart(p0: Animation?) {
                ObjectAnimator.ofFloat(cl_intro_art, "alpha", 0f).apply {
                    duration = 400
                    start()
                }.addListener(onEnd = {
                    Timber.e("Anim end")
                    viewModel._screenPosition.value = 1

                }

                )
            }
        })
        ivInroImage?.startAnimation(fade_in)
    }

    private fun initViewModel() {
        viewModel = requireActivity().obtainViewModel(IntroViewModel::class.java)
    }
}



