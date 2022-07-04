package com.example.easynote.features.register.state

sealed class RegisterState {
    object Loading : RegisterState()
    object Finished : RegisterState()
}