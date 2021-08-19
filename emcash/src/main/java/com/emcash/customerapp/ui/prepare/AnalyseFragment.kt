package com.emcash.customerapp.ui.prepare

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import kotlinx.android.synthetic.main.frame_analyse_score.*

class AnalyseFragment : Fragment(R.layout.frame_analyse_score) {

    private val viewModel: PrepareEmcashViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observe()


    }

    private fun observe() {
        viewModel.apply {
            profileData.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ApiCallStatus.SUCCESS -> {
                        renderUserDetails(it.data)
                    }
                }
            })
        }
    }

    private fun renderUserDetails(profileData: ProfileDetailsResponse.Data?) {
        profileData?.let { data ->
            viewModel.userName = data.name
            data.profileImage?.let {
                viewModel.dpUrl = it
            }
            val firstName = viewModel.userName.split(" ")
            tv_user_name.text ="Hello ".plus(firstName[0])
            tv_user_name.animate()
                .alpha(1f)
                .setDuration(1500)
                .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        (requireActivity() as PrepareEmCashActivity).animateCurve(onEnd = {
                            openPreparedFragment()
                        })
                    }, 1000)
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            })


        }

    }

    fun openPreparedFragment() {
        requireActivity().supportFragmentManager.commit {
            //     this.setCustomAnimations(android.R.anim.fade_out,android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<PreparedFragment>(R.id.container)
        }
    }
}