package com.example.easynote.features.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.features.register.state.RegisterState
import com.example.easynote.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Finished)
    val state = _state

    fun register(email: String, password: String) = viewModelScope.launch {
        repository.register(email, password)
            .onStart {
                _state.value = RegisterState.Loading
            }.catch { throwable ->
                _state.value = RegisterState.Error(throwable)
            }.map {
                _state.value = RegisterState.RegisteredSuccess(it)
            }.onCompletion {
                _state.value = RegisterState.Finished
            }.collect()
    }

}
