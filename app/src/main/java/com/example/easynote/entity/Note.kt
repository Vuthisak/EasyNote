package com.example.easynote.entity

import com.example.easynote.util.getCurrentDateTimeMilli
import java.io.Serializable

data class Note(
    var title: String? = null,
    var desc: String? = null
) : Serializable {
    var id: String? = null
    var userId: String? = null
    val updatedAt: Long = getCurrentDateTimeMilli()

    companion object {
        const val KEY_USER_ID = "userId"
    }
}
