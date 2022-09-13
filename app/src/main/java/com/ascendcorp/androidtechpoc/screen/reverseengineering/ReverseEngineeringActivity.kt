package com.ascendcorp.androidtechpoc.screen.reverseengineering

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.ascendcorp.androidtechpoc.databinding.ActivityReverseEngineeringBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity

class ReverseEngineeringActivity : BaseActivity<ActivityReverseEngineeringBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityReverseEngineeringBinding
        get() = {
            ActivityReverseEngineeringBinding.inflate(it)
        }

    private val susToken = "super_sus_token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this::class.java.simpleName, susToken)
    }
}
