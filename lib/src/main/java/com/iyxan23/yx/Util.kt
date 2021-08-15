package com.iyxan23.yx

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Int.sp: Float
    get() = TypedValue
                .applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    toFloat(),
                    Resources.getSystem().displayMetrics
                )