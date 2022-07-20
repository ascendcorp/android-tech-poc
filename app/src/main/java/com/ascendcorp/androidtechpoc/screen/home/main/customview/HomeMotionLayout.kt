package com.ascendcorp.androidtechpoc.screen.home.main.customview

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.TransitionRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.R
import kotlinx.parcelize.Parcelize
import kotlin.math.abs

internal class HomeMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val scaledTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
) : MotionLayout(context, attrs, defStyleAttr) {

    private val clContainer: ConstraintLayout?
        get() = getChildAt(3) as? ConstraintLayout

    private val ivBackground: ImageFilterView?
        get() = clContainer?.let { it.getChildAt(0) as? ImageFilterView }

    private val rvContent: RecyclerView?
        get() = clContainer?.let { it.getChildAt(1) as? RecyclerView }

    private val layBack: LinearLayout?
        get() = getChildAt(5) as? LinearLayout

    private var lastMotionY = 0f
    private var isDragging = false

    private val transitionListener: TransitionListener = object : TransitionListener {
        override fun onTransitionStarted(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int
        ) {
            if (startId == State.NORMAL.resId) {
                rvContent?.isVisible = true
            }
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
            if (endId == State.EXPANDED.resId) {
                ivBackground?.crossfade = progress
                rvContent?.alpha = 1f - progress
            }
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            if (startState == State.LOADING.resId)
                setTransition(Transition.COLLAPSE_VIEW_PAGER.resId)

            when (currentId) {
                State.EXPANDED.resId -> {
                    rvContent?.isVisible = false
                    onStateChangedListener?.onExpanded()
                }
                State.NORMAL.resId -> onStateChangedListener?.onNormal()
            }
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) = Unit
    }

    init {
        addTransitionListener(transitionListener)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // Workaround to fix nestedScrolling issue by changing transition manually
        val isNormalState = currentState == State.NORMAL.resId
        if (isNormalState) {
            if (isDraggingDown(target, dy)) setTransition(R.id.expandViewPager)
            else setTransition(R.id.collapseViewPager)
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    private fun isDraggingDown(target: View, dy: Int): Boolean {
        val isScrollingDown = dy < 0
        return isDragging and isScrollingDown and target.canScrollVertically(dy).not()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            ACTION_DOWN -> lastMotionY = ev.y
            ACTION_MOVE -> isDragging = abs(ev.y - lastMotionY) > scaledTouchSlop
            ACTION_UP -> {
                if (isDragging.not() && isEventInsideView(layBack, ev)) transitionToStart()
                isDragging = false
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun isEventInsideView(target: View?, event: MotionEvent?): Boolean {
        if (target?.isVisible == false || target?.alpha != 1f || event == null) return false

        val isEventXInsideViewX = target.left < event.x && event.x < target.right
        val isEventYInsideViewY = target.top < event.y && event.y < target.bottom
        return isEventXInsideViewX and isEventYInsideViewY
    }

    internal fun expandViewPager() {
        if (currentState != State.EXPANDED.resId) progress = 0f

        setTransition(Transition.EXPAND_VIEW_PAGER.resId)
        transitionToEnd()
    }

    private var onStateChangedListener: OnStateChangedListener? = null

    internal fun setOnStateChanged(onStateChangedListener: OnStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener
    }

    interface OnStateChangedListener {
        fun onExpanded()
        fun onNormal()
    }

    internal enum class State(@LayoutRes val resId: Int) {
        COLLAPSED(R.id.collapsed),
        EXPANDED(R.id.expanded),
        NORMAL(R.id.normal),
        LOADING(R.id.loading);

        companion object {
            fun getState(@LayoutRes resId: Int): State? {
                return values().find { it.resId == resId }
            }
        }
    }

    internal enum class Transition(@TransitionRes val resId: Int) {
        COLLAPSE_VIEW_PAGER(R.id.collapseViewPager),
        EXPAND_VIEW_PAGER(R.id.expandViewPager),
        LOAD_PAGE(R.id.loadPage);

        companion object {
            fun getTransition(@LayoutRes resId: Int): Transition? {
                return values().find { it.resId == resId }
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState(), startState, endState, targetPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? SavedState)?.run {
            super.onRestoreInstanceState(parcelable)

            setTransition(startState, endState)
            post { this@HomeMotionLayout.progress = progress }

            if (progress != 1f) return@run
            when (endState) {
                State.EXPANDED.resId -> onRestoredState?.onStateExpand()
            }
        }
    }

    private var onRestoredState: OnRestoredState? = null

    internal fun setOnRestoredState(onRestoredState: OnRestoredState) {
        this.onRestoredState = onRestoredState
    }

    internal interface OnRestoredState {
        fun onStateExpand()
    }

    @Parcelize
    private data class SavedState(
        val parcelable: Parcelable?,
        @LayoutRes val startState: Int,
        @LayoutRes val endState: Int,
        val progress: Float
    ) : Parcelable
}
