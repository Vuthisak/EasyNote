package com.example.easynote.features.register.state

import com.google.firebase.auth.FirebaseUser

sealed class RegisterState {
    object Loading : RegisterState()
    object Finished : RegisterState()
    data class Error(val ex: Throwable) : RegisterState()
    data class RegisteredSuccess(val user: FirebaseUser?) : RegisterState()
}