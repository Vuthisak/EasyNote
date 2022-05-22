package com.example.easynote.entity

import com.example.easynote.ui.theme.getRandomColor
import com.example.easynote.util.getCurrentDateTimeMilli
import java.io.Serializable

data class Note(
    var title: String? = null,
    var desc: String? = null
) : Serializable {
    var id: String? = null
    val updatedAt: Long = getCurrentDateTimeMilli()
    val backgroundColor: Long = getRandomColor()
}
