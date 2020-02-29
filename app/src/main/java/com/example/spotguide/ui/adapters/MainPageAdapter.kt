package com.example.map.ui.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.spotguide.core.base.BaseFragment

class MainPageAdapter(
    fm: FragmentManager,
    val pages: List<BaseFragment>
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = pages[position]
    override fun getCount() = pages.size
}