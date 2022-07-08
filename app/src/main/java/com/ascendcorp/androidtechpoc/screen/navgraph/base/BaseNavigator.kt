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
        bundle: Pair<String, Parcelable?> = Pair("bundle", null)
    )
}

abstract class BaseNavigatorImpl(
    protected val fragment: Fragment
) : BaseNavigator {

    private var navController: NavController? = null

    override fun findNavController(): NavController? {
        return navController ?: try {
            fragment.findNavController().apply {
                navController = this
            }
        } catch (e: IllegalStateException) {
            // Log Crashlytics as non-fatal for monitoring
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
        val navController = findNavController()
        val currentGraph = fragment.requireActivity().getResourceName(navController?.graph?.id)
        val currentDestination =
            fragment.requireActivity().getResourceName(navController?.currentDestination?.id)
        handleError(RuntimeException("Unsupported navigation on $currentGraph at $currentDestination"))
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


    private fun handleError(error: Throwable) {
        if (fragment is BaseFragment<*>) {
            fragment.displayError(error)
        } else {
            throw error
        }
    }
}
