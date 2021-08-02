package com.emcash.customerapp.ui.prepare

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.ChangeBounds
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.frame_emcash_prepared.*


class PreparedFragment : Fragment() {

    private val viewModel: PrepareEmcashViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 100
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }
        return inflater.inflate(R.layout.frame_emcash_prepared, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_lets_start.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                (iv_dp as View),
                "coin_image"
            )
            startActivity(Intent(requireActivity(), HomeActivity::class.java), options.toBundle())
            // requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        //    requireActivity().finish()

        }

        tv_info_how_it_work.setOnClickListener {
            viewModel._bottomSheetVisiblity.value = true
        }

    }

}