package com.example.spotguide.core.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseActivity
import com.example.spotguide.core.base.BaseFragment
import com.example.spotguide.core.extension.hideKeyboard
import com.example.spotguide.core.extension.simpleReplace
import java.io.Serializable
import kotlin.reflect.KClass


object Navigation {

    enum class Animation {
        HORIZONTAL, VERTICAL, FADE, NONE
    }

    // FRAGMENT

    fun <A: BaseActivity, F: BaseFragment, P: Serializable> switchFragments(activity: A, newFragment: F, params: P, animation: Animation = Animation.HORIZONTAL,
                                                                            clearBackStack: Boolean = false,
                                                                            useExistingFragment: Boolean = false)
    {
        activity.hideKeyboard()
        if (useExistingFragment) {
            val exists = checkIfFragmentInstanceExists(activity, newFragment)
            if (exists) {
                popToFragment(newFragment::class.java.simpleName, activity)
                return
            }
        }
        if (clearBackStack) clearBackStack(activity)
        val transition = activity.supportFragmentManager.beginTransaction()
        val arguments = Bundle()
        arguments.putSerializable("params", params)
        newFragment.arguments = arguments
        transition.simpleReplace(R.id.container, newFragment, animation)
    }

    fun <A: BaseActivity, F: BaseFragment> switchFragments(activity: A, newFragment: F, animation: Animation = Animation.HORIZONTAL,
                                                           clearBackStack: Boolean = false,
                                                           useExistingFragment: Boolean = false)
    {
        activity.hideKeyboard()
        if (useExistingFragment) {
            val exists = checkIfFragmentInstanceExists(activity, newFragment)
            if (exists) {
                popToFragment(newFragment::class.java.simpleName, activity)
                return
            }
        }
        if (clearBackStack) clearBackStack(activity)
        val transition = activity.supportFragmentManager.beginTransaction()
        transition.simpleReplace(R.id.container, newFragment, animation)
    }

    fun pop(activity: BaseActivity) {
        activity.hideKeyboard()
        val latestFragment = activity.supportFragmentManager.fragments.last() as? BaseFragment
        latestFragment?.let {
            if (it.isLastFragment) activity.finish()
            else activity.supportFragmentManager.popBackStack()
        } ?: kotlin.run { activity.supportFragmentManager.popBackStack() }
    }

    fun <T: Fragment>popToFragment(fragClass: KClass<T>, activity: BaseActivity) {
        activity.hideKeyboard()
        activity.supportFragmentManager.popBackStackImmediate(fragClass.simpleName, 0)
    }

    fun popToFragment(fragClassSimpleName: String, activity: BaseActivity) {
        activity.hideKeyboard()
        activity.supportFragmentManager.popBackStackImmediate(fragClassSimpleName, 0)
    }

    private fun clearBackStack(activity: BaseActivity) {
        val fm = activity.supportFragmentManager
        while (fm.backStackEntryCount > 0) {
            fm.popBackStackImmediate()
        }
    }

    private fun <F: BaseFragment> checkIfFragmentInstanceExists(activity: BaseActivity, fragment: F): Boolean {
        val backstack = activity.supportFragmentManager.fragments
        backstack.forEach {
            if (it.tag == fragment::class.java.simpleName) return true
        }
        return false
    }


    // ACTIVITY

    fun <A: BaseActivity> switchActivity(currentActivity: BaseActivity, activityClass: KClass<A>, noHistory: Boolean = true, animation: Animation = Animation.NONE) {
        currentActivity.hideKeyboard()
        val intent = Intent(currentActivity, activityClass.java)
        if (noHistory) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        currentActivity.startActivity(intent)
        when (animation) {
            Animation.HORIZONTAL -> {}
            Animation.VERTICAL -> {}
            else -> currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    fun switchActivity(currentActivity: BaseActivity, newIntent: Intent, noHistory: Boolean = true, animation: Animation = Animation.NONE) {
        currentActivity.hideKeyboard()
        if (noHistory) {
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        currentActivity.startActivity(newIntent)
        when (animation) {
            Animation.HORIZONTAL -> {}
            Animation.VERTICAL -> {}
            Animation.FADE -> currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            else -> currentActivity.overridePendingTransition(0, 0)
        }
    }
}