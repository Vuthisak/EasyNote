package com.example.easynote.features.forgotpassword.resetpassword.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState

class ResetPasswordUiState(
    val codeState: MutableState<String> = mutableStateOf(""),
    val newPasswordState: MutableState<String> = mutableStateOf(""),
    val confirmNewPasswordState: MutableState<String> = mutableStateOf("")
) : BaseUiState()
