package com.example.spotguide.features.main

import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.map.ui.adapters.MainPageAdapter
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseFragment
import com.example.spotguide.features.spot.MapFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_main

    private val fragments: List<BaseFragment>
        get() = listOf(MapFragment())

    override fun setViewModelStates() {}
    override fun setViewModelEvents() {}

    override fun setupUI() {
        setupViewPager()
        setupBottomNavbar()
    }

    private fun setupViewPager() {
        mainViewPager.adapter = MainPageAdapter(childFragmentManager, fragments)
        mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var previewItem: MenuItem? = null

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                previewItem?.let { it.isChecked = false }
                    ?: run { nav_view.menu.getItem(0).isChecked = false }
                nav_view.menu.getItem(position).isChecked = true
                previewItem = nav_view.menu.getItem(position)
            }
        })
    }

    private fun setupBottomNavbar() {
        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_map -> mainViewPager.currentItem = 0
                R.id.navigation_profile -> mainViewPager.currentItem = 1
            }
            true
        }
    }

    override fun handleBackPress(): Boolean {
        activity.finish()
        return true
    }
}