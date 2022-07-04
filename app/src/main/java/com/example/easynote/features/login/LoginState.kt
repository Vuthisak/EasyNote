package com.example.easynote.features.login

import com.google.firebase.auth.FirebaseUser

sealed class LoginState {
    object Loading : LoginState()
    object Finished : LoginState()
    data class Success(val user: FirebaseUser?) : LoginState()
    data class Error(val error: Throwable) : LoginState()
}