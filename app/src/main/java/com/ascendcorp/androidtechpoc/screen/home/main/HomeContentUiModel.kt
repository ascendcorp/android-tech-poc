package com.ascendcorp.androidtechpoc.screen.home.main

import kotlin.random.Random

internal data class HomeContentUiModel(
    val title: String,
    val homeContentDetails: List<HomeContentDetailUiModel>
)

internal data class HomeContentDetailUiModel(
    val title: String
)

internal fun getHomeContentUiModels(): List<HomeContentUiModel> {
    return List(HOME_CONTENT_SIZE) {
        getHomeContentUiModel(
            title = ('A'..'Z').toList()[it % 26].toString(),
            amountOfDetail = Random.nextInt(from = 2, until = HOME_CONTENT_DETAIL_MAX_SIZE)
        )
    }
}

private fun getHomeContentUiModel(title: String, amountOfDetail: Int): HomeContentUiModel {
    return HomeContentUiModel(
        title = "Home - $title",
        homeContentDetails = (1..amountOfDetail).map {
            HomeContentDetailUiModel(title = "$title - Title $it")
        }
    )
}

private const val HOME_CONTENT_SIZE = 30
private const val HOME_CONTENT_DETAIL_MAX_SIZE = 6
