package com.ascendcorp.androidtechpoc.screen.home.main.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ascendcorp.androidtechpoc.databinding.FragmentHomeDBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment

class HomeDFragment : BaseFragment<FragmentHomeDBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeDBinding
        get() = { inflater, viewGroup, attachToParent ->
            FragmentHomeDBinding.inflate(inflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {}
}
