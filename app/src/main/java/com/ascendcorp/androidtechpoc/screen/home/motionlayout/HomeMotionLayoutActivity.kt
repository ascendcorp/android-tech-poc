package com.ascendcorp.androidtechpoc.screen.home.motionlayout

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ascendcorp.androidtechpoc.databinding.ActivityHomeMotionLayoutBinding
import com.ascendcorp.androidtechpoc.screen.home.main.adapter.HomeContentAdapter
import com.ascendcorp.androidtechpoc.screen.home.main.getHomeContentUiModels
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity

class HomeMotionLayoutActivity : BaseActivity<ActivityHomeMotionLayoutBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeMotionLayoutBinding
        get() = { layoutInflater -> ActivityHomeMotionLayoutBinding.inflate(layoutInflater) }

    override fun setupView() {
        binding.rvContent.apply {
            adapter = HomeContentAdapter().apply { items = getHomeContentUiModels() }
            val layoutManager = LinearLayoutManager(context)
            this.layoutManager = layoutManager
        }
    }
}
