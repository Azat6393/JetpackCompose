package com.azatberdimyradov.jetpackcompose.utils

import android.content.res.Resources

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)