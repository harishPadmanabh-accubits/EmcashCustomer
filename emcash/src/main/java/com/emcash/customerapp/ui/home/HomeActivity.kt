package com.emcash.customerapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.utils.LevelProfileImageView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val users = listOf<DummyUserData>(
            DummyUserData(
                "Mary ",
                R.drawable.sample_dp,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Alan",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Milan",
                R.drawable.sample_dp,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Anise",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Fijui",
                R.drawable.sample_dp,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Lewis",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Kamal",
               null,
                LevelProfileImageView.UserProfileLevel.RED
            ), DummyUserData(
                "Michel",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.RED
            ))

    rv_recent_transactions.apply {
        layoutManager = GridLayoutManager(this@HomeActivity,5)
        adapter = RecentTransactionsAdapter(users)
    }

    }


}