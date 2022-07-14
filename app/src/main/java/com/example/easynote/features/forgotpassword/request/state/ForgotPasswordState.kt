package com.example.easynote.features.forgotpassword.request.state

sealed class ForgotPasswordState {
    object Loading : ForgotPasswordState()
    object Finished : ForgotPasswordState()
    object Success : ForgotPasswordState()
    data class Error(val ex: Throwable) : ForgotPasswordState()
}
