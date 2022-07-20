package com.ascendcorp.androidtechpoc.screen.home.motionlayout

import android.view.LayoutInflater
import com.ascendcorp.androidtechpoc.databinding.ActivityHomeBasicMotionLayoutBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity

class HomeBasicMotionLayoutActivity : BaseActivity<ActivityHomeBasicMotionLayoutBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBasicMotionLayoutBinding
        get() = { layoutInflater -> ActivityHomeBasicMotionLayoutBinding.inflate(layoutInflater) }
}
