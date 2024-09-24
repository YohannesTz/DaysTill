package com.github.yohannestz.daystill.ui.base.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    return sdf.format(Date(this))
}
