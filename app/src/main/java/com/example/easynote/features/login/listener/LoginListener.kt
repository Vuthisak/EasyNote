package com.example.easynote.features.login.listener

interface LoginListener {
    fun onLoginClicked(username: String, password: String)
    fun onCreateAccountClicked()
    fun onForgotPasswordClicked()
}