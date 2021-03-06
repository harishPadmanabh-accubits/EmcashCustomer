package com.emcash.customerapp.ui.intro
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.addListener
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.isNougatOrAbove
import com.emcash.customerapp.extensions.obtainViewModel
import kotlinx.android.synthetic.main.fragment_first_intro.*
import kotlinx.android.synthetic.main.fragment_first_intro.btn_next
import kotlinx.android.synthetic.main.fragment_second_intro.*
import kotlinx.android.synthetic.main.fragment_second_intro.view.*
import timber.log.Timber
class SecondIntroFragment : Fragment() {
    val viewModel: IntroViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second_intro, container, false).also {
            it.doOnLayout {
                performEnterTransition(it.iv_intro_coin)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener {
            openThirdIntroFragment()
        }
    }
    private fun performEnterTransition(target: AppCompatImageView) {
        val fade = ScaleAnimation(
            .4f,
            1f,
            0.4f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        fade.duration = 100
        fade.fillAfter = false
        fade.isFillEnabled = false
        fade.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }
            override fun onAnimationEnd(p0: Animation?) {
                Timber.e("Anim end")
            }
            override fun onAnimationStart(p0: Animation?) {
            }
        })
        target?.startAnimation(fade)
    }
    private fun perFormExitTransitionAndNavigate(target: AppCompatImageView) {
        val fade = ScaleAnimation(
            1f,
            .3f,
            1f,
            .3f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        fade.duration = 250
        fade.fillAfter = false
        fade.isFillEnabled = false
        fade.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }
            override fun onAnimationEnd(p0: Animation?) {
                Timber.e("Anim end")
                //viewModel._screenPosition.value = 2
            }
            override fun onAnimationStart(p0: Animation?) {
                ObjectAnimator.ofFloat(target, "alpha", .3f).apply {
                    duration = 150
                    start()
                }.addListener(onEnd = {
                    Timber.e("Anim end")
                    // viewModel._screenPosition.value =2
                    openThirdIntroFragment()
                }
                )
            }
        })
        target?.startAnimation(fade)
    }
    fun openThirdIntroFragment() {
        requireActivity().supportFragmentManager.commit {
            this.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            setReorderingAllowed(true)
            this.addSharedElement(iv_intro_coin, "small_coin")
            replace<ThirdIntroFragment>(R.id.container)
        }
    }
}