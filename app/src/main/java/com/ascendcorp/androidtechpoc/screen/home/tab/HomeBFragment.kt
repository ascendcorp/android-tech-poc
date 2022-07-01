package com.ascendcorp.androidtechpoc.screen.home.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ascendcorp.androidtechpoc.databinding.FragmentHomeBBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment

class HomeBFragment : BaseFragment<FragmentHomeBBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBBinding
        get() = { inflater, viewGroup, attachToParent ->
            FragmentHomeBBinding.inflate(inflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
    }
}
