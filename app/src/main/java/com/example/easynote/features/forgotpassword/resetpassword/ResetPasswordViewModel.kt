package com.example.easynote.features.forgotpassword.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.features.forgotpassword.resetpassword.state.ResetPasswordState
import com.example.easynote.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Finished)
    private val state = _state

    fun resetPassword(code: String, newPassword: String) = viewModelScope.launch {
        repository
            .confirmResetPassword(code, newPassword)
            .onStart {
                _state.value = ResetPasswordState.Loading
            }.catch { cause ->
                _state.value = ResetPasswordState.Error(cause)
            }.map {
                _state.value = ResetPasswordState.Success
            }.onCompletion {
                _state.value = ResetPasswordState.Finished
            }.collect()
    }

}
