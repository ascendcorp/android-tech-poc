package com.ascendcorp.androidtechpoc.screen.navgraph.base

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ascendcorp.androidtechpoc.extensions.getResourceName

interface BaseNavigator {

    val navHostFragmentId: Int

    fun findNavController(): NavController?

    fun navigateUp()

    fun popBackTo(@IdRes destinationId: Int, inclusive: Boolean = false)

    fun navigateToDestinationByDeepLink(
        destinationId: Int,
        bundle: Pair<String, Parcelable?> = Pair(BUNDLE, null)
    )
}

abstract class BaseNavigatorImpl(
    private val fragment: Fragment
) : BaseNavigator {

    private var navController: NavController? = null

    override fun findNavController(): NavController? {
        return navController ?: try {
            fragment.findNavController().also { navController = it }
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun navigateUp() {
        findNavController()?.navigateUp()
    }

    override fun popBackTo(@IdRes destinationId: Int, inclusive: Boolean) {
        findNavController()?.popBackStack(destinationId, inclusive)
    }

    protected fun unsupportedNavigation() {
        val (currentGraph, currentDestination) = with(fragment.requireActivity()) {
            getResourceName(navController?.graph?.id) to
                getResourceName(navController?.currentDestination?.id)
        }
        throw UnsupportedOperationException("$currentGraph at $currentDestination")
    }

    override fun navigateToDestinationByDeepLink(
        destinationId: Int,
        bundle: Pair<String, Parcelable?>
    ) {
        findNavController()?.run {
            createDeepLink()
                .setDestination(destinationId)
                .apply {
                    val (key, data) = bundle
                    setArguments(
                        Bundle().apply { putParcelable(key, data) }
                    )
                }
                .createPendingIntent()
                .send()
        } ?: unsupportedNavigation()
    }
}

internal const val BUNDLE = "bundle"
