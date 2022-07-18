package com.example.easynote.features.main.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState
import com.example.easynote.entity.Note
import com.google.accompanist.swiperefresh.SwipeRefreshState

class MainUiState(
    val items: MutableList<Note> = mutableStateListOf(),
    val isRefreshingState: SwipeRefreshState = SwipeRefreshState(false),
    val visibleState: MutableState<Boolean> = mutableStateOf(false),
) : BaseUiState()
