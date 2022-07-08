package com.ascendcorp.androidtechpoc.screen.main

import androidx.annotation.StringRes

data class TopicUiModel(
    @StringRes val titleRes: Int,
    val listener: () -> Unit
)
