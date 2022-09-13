package com.ascendcorp.androidtechpoc.screen.main

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.screen.navgraph.NavGraphActivity
import com.ascendcorp.androidtechpoc.screen.reverseengineering.ReverseEngineeringActivity

data class TopicUiModel(
    @StringRes val titleRes: Int,
    val listener: () -> Unit
)

fun Context.getTopics(): List<TopicUiModel> {
    return listOf(
        getTopic(R.string.topic_navigation_component, NavGraphActivity::class.java),
        getTopic(R.string.topic_reverse_engineering, ReverseEngineeringActivity::class.java)
    )
}

private fun Context.getTopic(titleRes: Int, activityClass: Class<*>): TopicUiModel {
    return TopicUiModel(
        titleRes = titleRes,
        listener = {
            startActivity(Intent(this, activityClass))
        }
    )
}
