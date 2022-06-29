package com.example.easynote.features.login

sealed class LoginState {
    object LOADING : LoginState()
    object FINISHED : LoginState()
}