package com.example.easynote.features.passcode.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState

data class PasscodeUiState(
    val passcodeState: MutableState<String> = mutableStateOf("")
) : BaseUiState()
