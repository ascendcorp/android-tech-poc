package com.ascendcorp.androidtechpoc.screen.home

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.tabs.TabLayout

class Params(
    val tabBarView: TabLayout,
    val bottomSheet: View,
    val recyclerView: RecyclerView
)

open class OnSwipeTouchListener(val context: Context) : View.OnTouchListener {
    private var initTabBarTopMargin = 0
    private lateinit var params: Params

    fun init(params: Params) {
        (params.tabBarView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            initTabBarTopMargin = topMargin
        }
        this.params = params
        bottomSheetBehavior = BottomSheetBehavior.from(params.bottomSheet)
//        Log.d("OnSwipe", "initTabBarTopMargin $initTabBarTopMargin")
    }

    private var mDownFocusY = 0f
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownFocusY = event.y
            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_MOVE -> {
                if (bottomSheetBehavior?.state != STATE_EXPANDED) return false
                val scrollY = mDownFocusY - event.y
                mDownFocusY = event.y
                if (scrollY != 0f) {
                    updateBottomSheetHeight(scrollY)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                mDownFocusY = 0f
            }
        }
        return false
    }

    var bottomSheetTop = 0

    private fun updateBottomSheetHeight(diffY: Float) {
        BottomSheetBehavior.from(params.bottomSheet).apply {
            val tabBarHeight = context.resources.getDimensionPixelSize(R.dimen.tab_bar_height)
            val offset = bottomSheetTop + diffY.toInt()
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            val height = displayMetrics.heightPixels
            val toolbarHeight = context.resources.getDimensionPixelSize(R.dimen.toolbar_height)
            bottomSheetTop = if (offset <= 0) {
                0
            } else if (offset > tabBarHeight) {
                tabBarHeight
            } else {
                offset
            }
            params.tabBarView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = bottomSheetTop * -1
            }
            params.recyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = bottomSheetTop
            }
            Log.d("bottom sheet top", bottomSheetTop.toString())
            this.maxHeight = height - tabBarHeight - toolbarHeight + bottomSheetTop
        }
    }
}