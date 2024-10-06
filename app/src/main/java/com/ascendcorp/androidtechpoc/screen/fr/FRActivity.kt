package com.ascendcorp.androidtechpoc.screen.fr

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.constraintlayout.widget.Guideline
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.databinding.ActivityFrBinding
import com.ascendcorp.androidtechpoc.screen.home.main.adapter.HomeContentAdapter
import com.ascendcorp.androidtechpoc.screen.home.main.getHomeContentUiModels
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FRActivity : BaseActivity<ActivityFrBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityFrBinding
        get() = {
            ActivityFrBinding.inflate(it)
        }

    private var rootViewWidth = 0
    private var textViewWidth = 0
    private var isAnimated = false

    override fun setupView() {
        binding.root.apply {
            setStatusBarColor(window, Color.TRANSPARENT, false)
            WindowCompat.setDecorFitsSystemWindows(window, false)
            ViewCompat.setOnApplyWindowInsetsListener(binding.g) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                (view as? Guideline)?.setGuidelineBegin(insets.top)
                windowInsets
            }

            doOnPreDraw { rootViewWidth = width }
        }

        binding.cl.apply {
            binding.ifv.setImageDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.teal_700)))
            binding.tv.apply {
                text = "Hello Worlddddddddd!"
                doOnPreDraw { textViewWidth = width }
            }
        }

        binding.rv.apply {
            adapter = HomeContentAdapter().apply { items = getHomeContentUiModels() }
            layoutManager = LinearLayoutManager(context)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                val startX = 0f + resources.getDimensionPixelSize(R.dimen.padding_margin_l)
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val firstItem = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    val centerX = ((rootViewWidth - textViewWidth) / 2).toFloat()

                    val (animator, onStart) = when {
                        firstItem == 0 -> {
                            val firstX = if (isAnimated) centerX else 0f
                            val animator = ObjectAnimator.ofFloat(binding.tv, TRANSLATION_X, firstX, startX)
                            val onStart = {
                                isAnimated = false
                                setStatusBarColor(window, Color.TRANSPARENT, false)
                                WindowCompat.setDecorFitsSystemWindows(window, false)
                                repeat(IMAGE_FILTER_STEP) {
                                    lifecycleScope.launch { delay(IMAGE_FILTER_DELAY) }
                                    binding.ifv.crossfade = 100f - ((it + 1) * IMAGE_FILTER_FACTER).toFloat()
                                }
                                binding.tv.setTextColor(Color.WHITE)
                            }
                            animator to onStart
                        }

                        isAnimated.not() -> {
                            val animator = ObjectAnimator.ofFloat(binding.tv, TRANSLATION_X, startX, centerX)
                            val onStart = {
                                isAnimated = true
                                setStatusBarColor(window, Color.TRANSPARENT, true)
                                WindowCompat.setDecorFitsSystemWindows(window, false)
                                repeat(IMAGE_FILTER_STEP) {
                                    lifecycleScope.launch { delay(IMAGE_FILTER_DELAY) }
                                    binding.ifv.crossfade = ((it + 1) * IMAGE_FILTER_FACTER).toFloat()
                                }
                                binding.tv.setTextColor(Color.BLACK)
                            }
                            animator to onStart
                        }

                        else -> null to null
                    }
                    animator?.let {
                        it.duration = DURATION
                        it.doOnStart { onStart?.invoke() }
                        it.start()
                    }
                }
            })
        }

        binding.prl.apply {
            setRefreshDrawable(CustomRingDrawable(context, this))
            setBackgroundColor(Color.WHITE)
            setOnRefreshListener {
                setRefreshing(true)
                lifecycleScope.launch {
                    delay(2000)
                    setRefreshing(false)
//                    binding.rv.updatePadding(top = 0)
                }
            }
//            setOnTouchListener { v, event ->
//                if (v.onTouchEvent(event)) {
//                    binding.rv.updatePadding(top = resources.getDimensionPixelSize(R.dimen.padding_margin_custom))
//                }
//                v.performClick()
//            }
        }
    }

    companion object {
        private const val TRANSLATION_X = "translationX"
        private const val DURATION = 300L
        private const val IMAGE_FILTER_STEP = 10
        private const val IMAGE_FILTER_FACTER = 10 // 100/IMAGE_FILTER_STEP
        private const val IMAGE_FILTER_DELAY = DURATION / IMAGE_FILTER_STEP
    }
}
