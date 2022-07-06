package com.example.easynote.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

abstract class BaseUiState(
    open val loadingState: MutableState<Boolean> = mutableStateOf(false)
)
