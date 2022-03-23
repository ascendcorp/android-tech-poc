package com.ascendcorp.androidtechpoc.screen.navgraph

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.databinding.ActivityNavGraphBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.NavGraphActivity.ActionType.KEEP_BACK_STACK
import com.ascendcorp.androidtechpoc.screen.navgraph.NavGraphActivity.ActionType.NORMAL
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity

class NavGraphActivity : BaseActivity<ActivityNavGraphBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityNavGraphBinding
        get() = { layoutInflater ->
            ActivityNavGraphBinding.inflate(layoutInflater)
        }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent, KEEP_BACK_STACK)
    }

    private fun handleDeepLink(intent: Intent?, actionType: ActionType) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        when (actionType) {
            NORMAL -> navController.handleDeepLink(intent)
            KEEP_BACK_STACK -> navController.navigate(Uri.parse(intent?.data.toString()))
        }
    }

    private enum class ActionType {
        NORMAL, KEEP_BACK_STACK
    }
}
