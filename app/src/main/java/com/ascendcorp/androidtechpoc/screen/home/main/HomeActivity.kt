package com.ascendcorp.androidtechpoc.screen.home.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.databinding.ActivityHomeBinding
import com.ascendcorp.androidtechpoc.extensions.getAttributeValue
import com.ascendcorp.androidtechpoc.extensions.getPixelOffset
import com.ascendcorp.androidtechpoc.screen.home.main.HomeTab.Companion.getTitleAndIcon
import com.ascendcorp.androidtechpoc.screen.home.main.adapter.HomeContentAdapter
import com.ascendcorp.androidtechpoc.screen.home.main.customview.HomeMotionLayout
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = { layoutInflater -> ActivityHomeBinding.inflate(layoutInflater) }

    private lateinit var homeContentAdapter: HomeContentAdapter

    private val mainTabPosition = HomeTab.D.ordinal

    override fun setupView() {
        updateHomeTab()
        updateContentLayout()
        updateContent()
    }

    private fun updateHomeTab() {
        with(binding) {
            val viewPager2 = vp2Container.apply {
                adapter = HomePagerAdapter(supportFragmentManager, lifecycle)
                updateViewPager2AndTabLayoutState(isEnable = false)
                setDefaultViewPagerItem()
            }
            TabLayoutMediator(tlTab, viewPager2) { tab: TabLayout.Tab, position: Int ->
                val (titleRes, iconRes) = getTitleAndIcon(position)
                if (titleRes == null || iconRes == null) return@TabLayoutMediator
                tab.apply {
                    text = getString(titleRes)
                    icon = resources.getDrawable(iconRes, theme)
                }
            }.attach()
        }
    }

    private fun updateViewPager2AndTabLayoutState(isEnable: Boolean) {
        with(binding) {
            vp2Container.isUserInputEnabled = isEnable
            tlTab.apply {
                val tabIndicatorHeight = if (isEnable) {
                    context.getPixelOffset(R.dimen.home_tab_indicator_height)
                } else {
                    0
                }
                setSelectedTabIndicatorHeight(tabIndicatorHeight)
            }
        }
    }

    private fun setDefaultViewPagerItem() {
        binding.vp2Container.setCurrentItem(mainTabPosition, false)
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val backgroundColorInt = when (tab?.position) {
                mainTabPosition -> getDefaultBackgroundColor()
                HomeTab.C.ordinal -> getAttributeValue(R.attr.homeBackgroundColor2nd)
                else -> getDefaultBackgroundColor()
            }
            updateBackgroundColor(backgroundColorInt)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    }

    private val customTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab?.position == HomeTab.C.ordinal) {
                updateBackgroundColor(getAttributeValue(R.attr.homeBackgroundColor2nd))
            }
            binding.vp2Container.setCurrentItem(tab?.position ?: 0, false)
            expandViewPager()
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

        override fun onTabReselected(tab: TabLayout.Tab?) {
            if (tab?.position == mainTabPosition) expandViewPager()
        }
    }

    private fun updateOnTabSelectedListener(isExpanded: Boolean) {
        // Disable smooth scrolling when click tab while viewPager is collapsed
        binding.tlTab.apply {
            if (isExpanded) {
                removeOnTabSelectedListener(customTabSelectedListener)
                addOnTabSelectedListener(onTabSelectedListener)
            } else {
                removeOnTabSelectedListener(onTabSelectedListener)
                addOnTabSelectedListener(customTabSelectedListener)
            }
        }
    }

    private fun expandViewPager() {
        binding.hmlContainer.expandViewPager()
    }

    @ColorInt
    private fun getDefaultBackgroundColor(): Int {
        return getAttributeValue(com.google.android.material.R.attr.colorSecondaryVariant)
    }

    private fun updateBackgroundColor(colorInt: Int) {
        with(binding) {
            window.statusBarColor = colorInt
            tlTab.setBackgroundColor(colorInt)
            hmlContainer.setBackgroundColor(colorInt)
        }
    }

    private fun updateContentLayout() {
        binding.hmlContainer.apply {
            val onStateChangedListener = object : HomeMotionLayout.OnStateChangedListener {
                override fun onExpanded() {
                    updateViewPager2AndTabLayoutState(isEnable = true)
                    updateOnTabSelectedListener(isExpanded = true)
                }

                override fun onNormal() {
                    updateViewPager2AndTabLayoutState(isEnable = false)
                    setDefaultViewPagerItem()
                    updateOnTabSelectedListener(isExpanded = false)
                }
            }
            setOnStateChanged(onStateChangedListener)

            val onRestoredState = object : HomeMotionLayout.OnRestoredState {
                override fun onStateExpand() {
                    updateViewPager2AndTabLayoutState(isEnable = true)
                }
            }
            setOnRestoredState(onRestoredState)
        }
    }

    private fun updateContent() {
        binding.rvContent.apply {
            adapter = HomeContentAdapter().apply { items = getHomeContentUiModels() }
                .also { homeContentAdapter = it }
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_VIEW_PAGER_ITEM_KEY, binding.vp2Container.currentItem)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        binding.vp2Container.currentItem = savedInstanceState.getInt(CURRENT_VIEW_PAGER_ITEM_KEY)
        super.onRestoreInstanceState(savedInstanceState)
    }
}
