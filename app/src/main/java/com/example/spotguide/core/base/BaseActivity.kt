package com.example.spotguide.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotguide.core.extension.visibleOrGone
import com.example.spotguide.core.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity: AppCompatActivity() {

    var authListener : FirebaseAuth.AuthStateListener? = null
    private var user: FirebaseUser? = null
    val loggedUserId get() = user!!.uid

    open fun onAuthStateChanged(user: FirebaseUser?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authListener = FirebaseAuth.AuthStateListener(object : FirebaseAuth.AuthStateListener, (FirebaseAuth) -> Unit {
            override fun invoke(p1: FirebaseAuth) {
                user = p1.getCurrentUser()
                onAuthStateChanged(user)
            }

            override fun onAuthStateChanged(p0: FirebaseAuth) {
                user = p0.getCurrentUser()
//                if (user != null){
//                    Log.i("SESION","secion iniciada  con email : ${user.email}")
//                }else{
//                    Log.i("SESION","secion cerrada 1 ")
//                }
                onAuthStateChanged(user)
            }
        })

        setupUI()
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (null != authListener) {
            FirebaseAuth.getInstance().removeAuthStateListener(authListener!!)
        }
    }

    open fun setupUI() {}

    override fun onBackPressed() {
        val allFragments = supportFragmentManager.fragments
        if (allFragments.isEmpty()) super.onBackPressed()
        var backPressHandled = false
        allFragments.forEach {
            if (it is BaseFragment) {
                backPressHandled = it.handleBackPress()
            }
            if (backPressHandled) return@forEach
        }
        if (!backPressHandled) Navigation.pop(this)
    }

    fun showLoading(text: String? = null){
        vLoading.visibleOrGone(true)
        tvLoadingText.text = text
        tvLoadingText.visibleOrGone(text != null)
    }
    fun hideLoading() = vLoading.visibleOrGone(false)
}