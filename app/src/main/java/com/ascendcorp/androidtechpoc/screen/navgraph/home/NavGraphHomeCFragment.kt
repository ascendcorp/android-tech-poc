package com.ascendcorp.androidtechpoc.screen.navgraph.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ascendcorp.androidtechpoc.databinding.FragmentNavGraphHomeCBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigator
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigatorImpl
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment

class NavGraphHomeCFragment : BaseFragment<FragmentNavGraphHomeCBinding>() {

    private val navigator: MainNavigator by lazy { MainNavigatorImpl(this) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNavGraphHomeCBinding
        get() = { layoutInflater, viewGroup, attachToParent ->
            FragmentNavGraphHomeCBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
        binding.bAction.setOnClickListener {
            navigator.navigateToHomeD(DISPLAY_TEXT)
        }
    }
}

private const val DISPLAY_TEXT = "Text from Home C"
