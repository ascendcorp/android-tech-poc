package com.ascendcorp.androidtechpoc.extensions

import android.content.Context
import androidx.annotation.IdRes

fun Context.getResourceName(@IdRes resId: Int?): String? =
    resId?.let { resources.getResourceName(resId) }
