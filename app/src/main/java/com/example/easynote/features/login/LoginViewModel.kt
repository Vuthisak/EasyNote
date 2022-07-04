package com.example.easynote.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.features.login.state.LoginState
import com.example.easynote.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private var _state = MutableStateFlow<LoginState>(LoginState.Finished)
    val state = _state.asStateFlow()

    fun login(email: String, password: String) = viewModelScope.launch {
        repository
            .login(email, password)
            .onStart {
                _state.value = LoginState.Loading
            }
            .catch {
                _state.value = LoginState.Error(it)
            }
            .onCompletion {
                _state.value = LoginState.Finished
            }
            .collect {
                _state.value = LoginState.Success(it)
            }
    }

}