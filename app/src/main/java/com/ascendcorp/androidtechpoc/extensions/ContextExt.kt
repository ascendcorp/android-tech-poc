package com.ascendcorp.androidtechpoc.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.annotation.IdRes

fun Context.getResourceName(@IdRes resId: Int?): String? =
    resId?.let { resources.getResourceName(resId) }

fun Context.getPixelOffset(@DimenRes dimenRes: Int): Int {
    return resources.getDimensionPixelOffset(dimenRes)
}

fun Context.getAttributeValue(@AttrRes attrRes: Int, typedValue: TypedValue = TypedValue()): Int {
    return typedValue.apply { theme.resolveAttribute(attrRes, this, true) }.data
}
