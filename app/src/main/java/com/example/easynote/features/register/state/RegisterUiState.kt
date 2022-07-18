package com.example.easynote.features.register.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState

data class RegisterUiState(
    val usernameState: MutableState<String> = mutableStateOf(""),
    val passwordState: MutableState<String> = mutableStateOf(""),
    val confirmPasswordState: MutableState<String> = mutableStateOf("")
) : BaseUiState()
