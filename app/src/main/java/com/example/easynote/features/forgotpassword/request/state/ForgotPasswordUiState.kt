package com.example.easynote.features.forgotpassword.request.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState

data class ForgotPasswordUiState(
    val emailState: MutableState<String> = mutableStateOf(""),
    override val loadingState: MutableState<Boolean> = mutableStateOf(false)
) : BaseUiState(loadingState)
