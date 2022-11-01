package com.ascendcorp.androidtechpoc.screen.home.list

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.databinding.ActivityMainBinding
import com.ascendcorp.androidtechpoc.screen.home.main.HomeActivity
import com.ascendcorp.androidtechpoc.screen.home.motionlayout.HomeBasicMotionLayoutActivity
import com.ascendcorp.androidtechpoc.screen.home.motionlayout.HomeMotionLayoutActivity
import com.ascendcorp.androidtechpoc.screen.main.TopicAdapter
import com.ascendcorp.androidtechpoc.screen.main.TopicUiModel
import com.ascendcorp.androidtechpoc.screen.main.getTopic
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity

class HomeListActivity : BaseActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = { layoutInflater -> ActivityMainBinding.inflate(layoutInflater) }

    override fun setupView() {
        with(binding) {
            toolbar.title = getString(R.string.topic_home)
            rvTopic.apply {
                adapter = TopicAdapter().apply { items = getTopics() }
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun getTopics(): List<TopicUiModel> {
        return listOf(
            getTopic(
                R.string.topic_home_basic_motion_layout,
                HomeBasicMotionLayoutActivity::class.java
            ),
            getTopic(R.string.topic_home_motion_layout, HomeMotionLayoutActivity::class.java),
            getTopic(R.string.topic_home, HomeActivity::class.java)
        )
    }
}
