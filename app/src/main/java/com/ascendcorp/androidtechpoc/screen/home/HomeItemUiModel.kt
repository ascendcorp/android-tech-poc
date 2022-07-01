package com.ascendcorp.androidtechpoc.screen.home

import androidx.annotation.DrawableRes

data class HomeItemUiModel(
    @DrawableRes val imageRes: Int,
    val title: String,
    val itemClick: () -> Unit
)
