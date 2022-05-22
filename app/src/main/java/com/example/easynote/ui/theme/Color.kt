package com.example.easynote.ui.theme

import androidx.compose.ui.graphics.Color
import java.util.*

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xE6DE00EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val BackgroundLoading = Color(0x80615E5E)

fun getRandomColor(): Long {
    val colors =
        arrayOf(
            0xE6FFEB3B,
            0xE6FF00FF,
            0xE60000FF,
            0xE6888888,
            0xE600FFFF,
            0xE62B8582,
            0xE6FFA200,
            0xE6FF001E,
            0xE6C58C8C,
            0xE6174BA3,
            0xE6FFED3B,
            0xE6FF0EFF,
            0xE6000AFF,
            0xE6888288,
            0xE600FFAF,
            0xE62B8182,
            0xE6FFA500,
            0xE6FF021E,
            0xE6C58E8C,
            0xE6174EA3
        )
    val random = Random()
    return colors[random.nextInt(colors.size)]
}
