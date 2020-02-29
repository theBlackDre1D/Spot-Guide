package com.example.spotguide.core.extension

import androidx.fragment.app.FragmentTransaction
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseFragment
import com.example.spotguide.core.navigation.Navigation

fun <F: BaseFragment> FragmentTransaction.simpleReplace(frameLayoutID: Int, fragment: F, animation: Navigation.Animation) {
    when (animation) {
        Navigation.Animation.HORIZONTAL -> { setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right) }
        Navigation.Animation.VERTICAL -> { setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_alpha, 0, R.anim.exit_to_bottom) }
        else -> { /*exit_alpha*/ }
    }
    replace(frameLayoutID, fragment, fragment::class.simpleName)
    addToBackStack(fragment::class.simpleName)
    commit()
}