package com.ascendcorp.androidtechpoc.screen.home.motionlayout.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.R
import kotlin.math.abs

class CustomMotionLayout @JvmOverloads constructor(
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

    private val tvBack: TextView?
        get() = getChildAt(4) as? TextView

    private var lastMotionY = 0f
    private var isDragging = false

    private var lastStartState = 0

    private val transitionListener: TransitionListener = object : TransitionListener {
        override fun onTransitionStarted(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int
        ) = Unit

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
            if (endId == R.id.expanded) {
                ivBackground?.crossfade = progress
                rvContent?.apply {
                    alpha = 1f - progress
                    isVisible = (alpha > 0.1f)
                }
            }

            lastStartState = startId
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) = Unit

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
        // Workaround to fix nestedScrolling issue with changing transition manually
        when {
            isDraggingDownAtNormalState(target, dy) -> setTransition(R.id.expandViewPager)
            isNormalState() && currentState != R.id.loading -> setTransition(R.id.collapseViewPager)
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    private fun isDraggingDownAtNormalState(target: View, dy: Int): Boolean {
        val isScrollingDown = dy < 0
        return isDragging and
            isScrollingDown and
            target.canScrollVertically(dy).not() and
            isNormalState()
    }

    private fun isNormalState(): Boolean = currentState == R.id.normal

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            ACTION_DOWN -> lastMotionY = ev.y
            ACTION_MOVE -> isDragging = abs(ev.y - lastMotionY) > scaledTouchSlop
            ACTION_UP -> {
                if (isDragging.not() && isEventInsideView(tvBack, ev)) transitionToStart()
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
}
