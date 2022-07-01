package com.ascendcorp.androidtechpoc.screen.home.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ascendcorp.androidtechpoc.databinding.FragmentHomeCBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment

class HomeCFragment : BaseFragment<FragmentHomeCBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeCBinding
        get() = { inflater, viewGroup, attachToParent ->
            FragmentHomeCBinding.inflate(inflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
    }
}
