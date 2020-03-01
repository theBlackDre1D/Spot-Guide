package com.example.spotguide.features.splash

import android.os.Bundle
import android.os.Handler
import com.example.spotguide.core.base.BaseActivity
import com.example.spotguide.core.navigation.Navigation
import com.example.spotguide.features.main.MainActivity

class SplashActivity : BaseActivity() {

    private val splashDuration = 2L * 1000L // two seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         *  Not good practice only for presentation of correct splash screen.
         *  Here can be performed some calculations or download necessary information and then
         *  proceed next
         */
        Handler().postDelayed({
            Navigation.switchActivity(this, MainActivity::class, true)
        }, splashDuration)
    }
}
