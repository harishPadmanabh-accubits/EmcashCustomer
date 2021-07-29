package com.emcash.customerapp.ui.intro
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.activityViewModels
import androidx.transition.ChangeBounds
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import kotlinx.android.synthetic.main.fragment_first_intro.*
import kotlinx.android.synthetic.main.fragment_first_intro.btn_next
import kotlinx.android.synthetic.main.fragment_third_intro.*
class ThirdIntroFragment : Fragment() {
    val viewModel: IntroViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 500
        }
        return inflater.inflate(R.layout.fragment_third_intro, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performEnterAnimation()
        btn_next.setOnClickListener {
            viewModel._screenPosition.value = 3
        }
    }
    fun performEnterAnimation() {
        val scale = ScaleAnimation(
            .8f,
            1f,
            .8f,
            1f,
            Animation.RELATIVE_TO_SELF,
            .8f,
            Animation.RELATIVE_TO_SELF,
            .8f
        ).also {
            it.duration = 500
            it.fillAfter = true
        }
        iv_phones.startAnimation(scale)
    }
}