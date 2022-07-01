package com.ascendcorp.androidtechpoc.screen.home

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.databinding.ActivityHomeBinding
import com.ascendcorp.androidtechpoc.screen.home.HomeTab.Companion.getTitleAndIcon
import com.ascendcorp.androidtechpoc.screen.main.TopicAdapter
import com.ascendcorp.androidtechpoc.screen.main.TopicUiModel
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator


class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = { layoutInflater ->
            ActivityHomeBinding.inflate(layoutInflater)
        }

    private val onTouch = object : OnSwipeTouchListener(this@HomeActivity) {}

    override fun setupView() {
        updateHomeTab()
        updateContent()
        binding.rvContent.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        onTouch.init(
            Params(
                binding.tlTab,
                binding.flContent,
                binding.rvContent,
            )
        )
        binding.rvContent.setOnTouchListener(onTouch)
        binding.root.post {
            val parentWidth = binding.root.measuredWidth
            val scale = resources.displayMetrics.density
            val intMargin = ITEM_COUNT * 10 * scale + 0.5f
            var intActualWidth: Int = ((parentWidth - intMargin) / ITEM_COUNT).toInt()
            intActualWidth = ((parentWidth + intActualWidth - intMargin) / ITEM_COUNT + 1).toInt()

            val imageRes = R.drawable.ic_home_a

            binding.rvContent.adapter = ConcatAdapter(TopicAdapter().apply {
                topics = listOf(
                    TopicUiModel(
                        R.string.home_tab_a
                    ) {

                    },
                    TopicUiModel(
                        R.string.home_tab_b
                    ) {

                    },
                    TopicUiModel(
                        R.string.home_tab_c
                    ) {

                    },
                    TopicUiModel(
                        R.string.home_tab_c
                    ) {

                    }
                )
            }, HomeScrollItemAdapter(intActualWidth).apply {
                items = listOf(
                    HomeScrollItemUiModel(
                        TITLE,
                        listOf(
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                        )
                    )
                )
            }, HomeScrollItemAdapter(intActualWidth).apply {
                items = listOf(
                    HomeScrollItemUiModel(
                        TITLE,
                        listOf(
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                        )
                    )
                )
            }, HomeScrollItemAdapter(intActualWidth).apply {
                items = listOf(
                    HomeScrollItemUiModel(
                        TITLE,
                        listOf(
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                            HomeItemUiModel(
                                imageRes = imageRes,
                                title = TITLE,
                                itemClick = {

                                }
                            ),
                        )
                    )
                )
            })
        }
    }

    private fun updateHomeTab() {
        with(binding) {
            val viewPager2 = vp2Container.apply {
                adapter = HomePagerAdapter(supportFragmentManager, lifecycle)
            }
            TabLayoutMediator(tlTab, viewPager2) { tab, position ->
                val (titleRes, iconRes) = getTitleAndIcon(position)
                if (titleRes == null || iconRes == null) return@TabLayoutMediator
                tab.apply {
                    text = getString(titleRes)
                    icon = ContextCompat.getDrawable(root.context, iconRes)
                    this.customView
                }
            }.attach()
        }
    }

    private fun updateContent() {
        var initToolbarTopMargin: Int
        var layoutParams: ViewGroup.MarginLayoutParams
        (binding.headerView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            initToolbarTopMargin = topMargin
            layoutParams = this
        }
        BottomSheetBehavior.from(binding.flContent).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            val height = displayMetrics.heightPixels
            maxHeight =
                height - resources.getDimensionPixelSize(R.dimen.tab_bar_height) - resources.getDimensionPixelSize(
                    R.dimen.toolbar_height
                )
            isHideable = false
            addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        onSlide(slideOffset, initToolbarTopMargin, layoutParams)
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                bottomSheet.setBackgroundResource(R.drawable.bg_home_content)
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                bottomSheet.setBackgroundResource(R.drawable.bg_home_content_collapse)
                            }
                            else -> Unit
                        }
                    }
                }
            )
        }
    }

    private fun onSlide(
        slideOffset: Float,
        initToolbarTopMargin: Int,
        layoutParams: ViewGroup.MarginLayoutParams
    ) {
        var offset = slideOffset - 0.8
        if (offset <= 0) {
            offset = 0.0
        }
        val margin =
            ((offset / MAX_OFFSET * binding.headerView.height) - binding.headerView.height) + initToolbarTopMargin
        layoutParams.topMargin = margin.toInt()
        binding.headerView.layoutParams = layoutParams
    }

    companion object {
        const val MAX_OFFSET = 0.2
        const val ITEM_COUNT = 4.5
    }
}

private const val TITLE = "title"
