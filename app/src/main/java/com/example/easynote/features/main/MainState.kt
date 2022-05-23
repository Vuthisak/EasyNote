package com.example.easynote.features.main

import com.example.easynote.entity.Note

sealed class MainState {
    object Finished : MainState()
    object Loading : MainState()
    data class Error(val ex: Throwable?) : MainState()
    data class OnGetListSuccess(
        val items: MutableList<Note>, val isReloaded: Boolean = false
    ) : MainState()
}
