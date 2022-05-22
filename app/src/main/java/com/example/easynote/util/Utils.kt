package com.example.easynote.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

fun getCurrentDateTimeMilli() = System.currentTimeMillis()

fun String?.getOrDefault(): String = this ?: ""

@SuppressLint("SimpleDateFormat")
fun Long.formattedDate(): String {
    val dateFormat = SimpleDateFormat("dd MMM, yyyy hh:mm:ss a")
    return dateFormat.format(this)
}