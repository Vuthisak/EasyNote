package com.example.easynote.features.login.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState

class LoginUiState(
    val usernameState: MutableState<String> = mutableStateOf(""),
    val passwordState: MutableState<String> = mutableStateOf("")
) : BaseUiState()
