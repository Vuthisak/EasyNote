package com.example.easynote.features.forgotpassword.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.features.forgotpassword.request.state.ForgotPasswordState
import com.example.easynote.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Finished)
    val state = _state

    fun requestForgotPassword(email: String) = viewModelScope.launch {
        repository.requestResetPassword(email)
            .onStart {
                _state.value = ForgotPasswordState.Loading
            }.catch { cause ->
                throw  cause
                _state.value = ForgotPasswordState.Error(cause)
            }.map {
                _state.value = ForgotPasswordState.Success
            }.onCompletion {
                _state.value = ForgotPasswordState.Finished
            }.collect()
    }

}
