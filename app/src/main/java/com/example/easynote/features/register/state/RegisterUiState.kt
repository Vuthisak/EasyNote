package com.example.easynote.features.register.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class RegisterUiState(
    val loadingState: MutableState<Boolean> = mutableStateOf(false)
)
