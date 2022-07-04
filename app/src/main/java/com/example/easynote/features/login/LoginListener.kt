package com.example.easynote.features.login

interface LoginListener {
    fun onLoginClicked(username: String, password: String)
    fun onCreateAccountClicked()
}