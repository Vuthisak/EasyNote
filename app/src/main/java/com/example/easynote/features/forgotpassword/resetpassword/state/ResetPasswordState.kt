package com.example.easynote.features.forgotpassword.resetpassword.state

sealed class ResetPasswordState {
    object Loading : ResetPasswordState()
    object Finished : ResetPasswordState()
    object Success : ResetPasswordState()
    data class Error(val ex: Throwable) : ResetPasswordState()
}
