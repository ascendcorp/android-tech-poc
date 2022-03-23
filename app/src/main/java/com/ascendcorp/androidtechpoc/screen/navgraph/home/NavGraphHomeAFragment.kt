package com.ascendcorp.androidtechpoc.screen.navgraph.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.databinding.FragmentNavGraphHomeABinding
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigator
import com.ascendcorp.androidtechpoc.screen.navgraph.MainNavigatorImpl
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragment.ActionType.DEEPLINK
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragment.ActionType.MAIN_NAVIGATOR
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragment.ActionType.MAIN_NAVIGATOR_DEEP_LINK
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragment.ActionType.NORMAL
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragment.ActionType.NORMAL_WITH_DESTINATION
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragment.ActionType.SAFE_ARGS

class NavGraphHomeAFragment : BaseFragment<FragmentNavGraphHomeABinding>() {

    private val navigator: MainNavigator by lazy { MainNavigatorImpl(this) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNavGraphHomeABinding
        get() = { layoutInflater, viewGroup, attachToParent ->
            FragmentNavGraphHomeABinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
        with(binding) {
            bAction.setOnClickListener {
                navigateToDestination(MAIN_NAVIGATOR)
            }

            bBackAction.setOnClickListener {
                requireActivity().finish()
            }
        }
    }

    private fun navigateToDestination(actionType: ActionType) {
        when (actionType) {
            NORMAL -> findNavController().navigate(R.id.action_navGraphHomeAFragment_to_navGraphHomeBFragment)
            NORMAL_WITH_DESTINATION -> findNavController().navigate(R.id.navGraphHomeCFragment)
            DEEPLINK -> findNavController().navigate(Uri.parse("androidtechpoc://navgraph/homeC"))
            SAFE_ARGS -> {
                val action = NavGraphHomeAFragmentDirections
                    .actionNavGraphHomeAFragmentToNavGraphHomeBFragment()
                findNavController().navigate(action)
            }
            MAIN_NAVIGATOR -> navigator.navigateToHomeB()
            MAIN_NAVIGATOR_DEEP_LINK -> navigator.navigateToDestinationByDeepLink(
                destinationId = R.id.navGraphHomeDFragment,
                bundle = "bundle" to NavGraphHomeDBundle("HomeId", "Home")
            )
        }
    }

    private enum class ActionType {
        NORMAL, NORMAL_WITH_DESTINATION, DEEPLINK, SAFE_ARGS, MAIN_NAVIGATOR, MAIN_NAVIGATOR_DEEP_LINK
    }
}
