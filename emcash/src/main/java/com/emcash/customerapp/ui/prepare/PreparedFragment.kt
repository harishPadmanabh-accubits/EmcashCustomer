package com.emcash.customerapp.ui.prepare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import com.emcash.customerapp.R

class PreparedFragment:Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            sharedElementEnterTransition = ChangeBounds().apply {
                duration = 100
            }
            sharedElementReturnTransition= ChangeBounds().apply {
                duration = 750
            }
            return inflater.inflate(R.layout.frame_emcash_prepared, container, false)
        }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}