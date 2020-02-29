package com.example.spotguide.core

import android.app.Activity
import android.app.Application
import android.content.ContextWrapper
import android.os.Bundle
import com.example.spotguide.core.base.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.pixplicity.easyprefs.library.Prefs
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.lang.ref.WeakReference

class App: Application() {

    private val db: FirebaseFirestore by inject()

    companion object {

        lateinit var currentActivity: WeakReference<BaseActivity>
            private set
    }

    override fun onCreate() {
        super.onCreate()

        registerCallbacks()
        initKoin()
        initPrefs()
        setupFirestoreSettings()
        setupFirebaseNotifications()
    }

    private fun registerCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {}
            override fun onActivityResumed(activity: Activity?) {}
            override fun onActivityStarted(activity: Activity?) {}
            override fun onActivityDestroyed(activity: Activity?) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
            override fun onActivityStopped(activity: Activity?) {}
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity is BaseActivity) {
                    currentActivity = WeakReference(activity)
                }
            }
        })
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@App)
            modules(appModules)
        }
    }

    private fun setupFirebaseNotifications() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) return@OnCompleteListener
                // Get new Instance ID token
                val token = task.result?.token
            })

        FirebaseMessaging.getInstance().subscribeToTopic("infos")
    }

    private fun setupFirestoreSettings() {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    private fun initPrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}