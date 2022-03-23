package com.ascendcorp.androidtechpoc.screen.navgraph.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ascendcorp.androidtechpoc.databinding.FragmentNavGraphHomeBBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigator
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigatorImpl
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment

class NavGraphHomeBFragment : BaseFragment<FragmentNavGraphHomeBBinding>() {

    private val navigator: MainNavigator by lazy { MainNavigatorImpl(this) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNavGraphHomeBBinding
        get() = { layoutInflater, viewGroup, attachToParent ->
            FragmentNavGraphHomeBBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
        with(binding) {
            bAction.setOnClickListener {
                navigator.navigateToHomeC()
            }

            bSubAction.setOnClickListener {
                navigator.navigateToHomeCSub()
            }
        }
    }
}
