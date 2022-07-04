package com.example.easynote.features.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.repository.AuthRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    fun register(email: String, password: String) = viewModelScope.launch {
        repository.register(email, password)
            .onStart {

            }.catch {

            }.onCompletion {

            }.collect {

            }
    }

}
