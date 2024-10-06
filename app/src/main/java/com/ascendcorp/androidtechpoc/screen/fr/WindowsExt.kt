package com.ascendcorp.androidtechpoc.screen.fr

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt

fun setStatusBarColor(
    window: Window,
    @ColorInt colorInt: Int,
    isWindowLightStatusBar: Boolean = true
) {
    setTranslucentStatusBarColor(window, false, isWindowLightStatusBar)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.statusBarColor = colorInt
    }
}

fun setTranslucentStatusBarColor(
    window: Window,
    isTranslucent: Boolean = true,
    isWindowLightStatusBar: Boolean = true
) {
    with(window) {
        decorView.systemUiVisibility = 0 // clear
        if (isTranslucent) {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = if (isWindowLightStatusBar) {
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }
}
