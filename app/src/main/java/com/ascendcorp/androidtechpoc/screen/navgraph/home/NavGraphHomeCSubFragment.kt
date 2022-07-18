package com.ascendcorp.androidtechpoc.screen.navgraph.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ascendcorp.androidtechpoc.databinding.FragmentNavGraphHomeCSubBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigator
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigatorImpl
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeCSubFragment.ActionType.MAIN_NAVIGATOR
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeCSubFragment.ActionType.NORMAL

class NavGraphHomeCSubFragment : BaseFragment<FragmentNavGraphHomeCSubBinding>() {

    private val navigator: MainNavigator by lazy { MainNavigatorImpl(this) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNavGraphHomeCSubBinding
        get() = { layoutInflater, viewGroup, attachToParent ->
            FragmentNavGraphHomeCSubBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
        binding.bAction.setOnClickListener {
            navigateToDestination(MAIN_NAVIGATOR)
        }
    }

    private fun navigateToDestination(actionType: ActionType) {
        when (actionType) {
            NORMAL -> {
                val directions = NavGraphHomeCSubFragmentDirections
                    .actionNavGraphHomeCSubFragmentToNavGraphHomeDFragment(
                        displayText = DISPLAY_TEXT,
                        bundle = NavGraphHomeDBundle(
                            findNavController().currentDestination?.id.toString(),
                            findNavController().currentDestination?.label.toString()
                        )
                    )
                findNavController().navigate(directions)
            }
            MAIN_NAVIGATOR -> navigator.navigateToHomeD(DISPLAY_TEXT)
        }
    }

    private enum class ActionType {
        NORMAL, MAIN_NAVIGATOR
    }
}

private const val DISPLAY_TEXT = "Text from Home C Sub"
