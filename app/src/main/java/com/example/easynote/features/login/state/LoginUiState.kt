package com.example.easynote.features.login.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class LoginUiState(
    val usernameState: MutableState<String> = mutableStateOf(""),
    val passwordState: MutableState<String> = mutableStateOf(""),
    val loadingState: MutableState<Boolean> = mutableStateOf(false)
)