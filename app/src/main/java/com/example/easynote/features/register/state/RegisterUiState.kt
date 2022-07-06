package com.example.easynote.features.register.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class RegisterUiState(
    val usernameState: MutableState<String> = mutableStateOf(""),
    val passwordState: MutableState<String> = mutableStateOf(""),
    val confirmPasswordState: MutableState<String> = mutableStateOf(""),
    val loadingState: MutableState<Boolean> = mutableStateOf(false)
) {
    fun showLoading() = run { loadingState.value = true }
    fun hideLoading() = run { loadingState.value = false }
}
