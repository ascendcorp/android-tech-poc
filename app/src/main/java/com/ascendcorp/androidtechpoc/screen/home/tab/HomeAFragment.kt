package com.ascendcorp.androidtechpoc.screen.home.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ascendcorp.androidtechpoc.databinding.FragmentHomeABinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment

class HomeAFragment : BaseFragment<FragmentHomeABinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeABinding
        get() = { inflater, viewGroup, attachToParent ->
            FragmentHomeABinding.inflate(inflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
    }
}
