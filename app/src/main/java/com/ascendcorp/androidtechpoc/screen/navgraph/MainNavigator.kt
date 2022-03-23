package com.ascendcorp.androidtechpoc.screen.navgraph

import androidx.fragment.app.Fragment
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseNavigator
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseNavigatorImpl
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeAFragmentDirections
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeBFragmentDirections
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeCFragmentDirections
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeCSubFragmentDirections
import com.ascendcorp.androidtechpoc.screen.navgraph.home.NavGraphHomeDBundle

interface MainNavigator : BaseNavigator {

    fun navigateToHomeB()

    fun navigateToHomeC()

    fun navigateToHomeCSub()

    fun navigateToHomeD(displayText: String)
}

class MainNavigatorImpl(fragment: Fragment) : BaseNavigatorImpl(fragment), MainNavigator {

    override val navHostFragmentId: Int
        get() = R.id.navHostFragment

    override fun navigateToHomeB() {
        val navController = findNavController()
        when (navController?.currentDestination?.id) {
            R.id.navGraphHomeAFragment -> navController.navigate(
                NavGraphHomeAFragmentDirections
                    .actionNavGraphHomeAFragmentToNavGraphHomeBFragment()
            )
            else -> unsupportedNavigation()
        }
    }

    override fun navigateToHomeC() {
        val navController = findNavController()
        when (navController?.currentDestination?.id) {
            R.id.navGraphHomeBFragment -> navController.navigate(
                NavGraphHomeBFragmentDirections
                    .actionNavGraphHomeBFragmentToNavGraphHomeCFragment()
            )
            else -> unsupportedNavigation()
        }
    }

    override fun navigateToHomeCSub() {
        val navController = findNavController()
        when (navController?.currentDestination?.id) {
            R.id.navGraphHomeBFragment -> navController.navigate(
                NavGraphHomeBFragmentDirections
                    .actionNavGraphHomeBFragmentToNavGraphHomeCSubFragment()
            )
            else -> unsupportedNavigation()
        }
    }

    override fun navigateToHomeD(displayText: String) {
        val navController = findNavController()

        val bundle = NavGraphHomeDBundle(
            navController?.currentDestination?.id.toString(),
            navController?.currentDestination?.label.toString()
        )

        when (navController?.currentDestination?.id) {
            R.id.navGraphHomeCFragment -> navController.navigate(
                NavGraphHomeCFragmentDirections
                    .actionNavGraphHomeCFragmentToNavGraphHomeDFragment(displayText, bundle)
            )
            R.id.navGraphHomeCSubFragment -> navController.navigate(
                NavGraphHomeCSubFragmentDirections
                    .actionNavGraphHomeCSubFragmentToNavGraphHomeDFragment(displayText, bundle)
            )
            else -> unsupportedNavigation()
        }
    }
}
