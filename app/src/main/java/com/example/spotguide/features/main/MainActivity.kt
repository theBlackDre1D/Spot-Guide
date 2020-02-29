package com.example.spotguide.features.main

import android.os.Bundle
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseActivity
import com.example.spotguide.core.navigation.Navigation

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigation.switchFragments(this,
            MainFragment(), Navigation.Animation.NONE)
    }
}
