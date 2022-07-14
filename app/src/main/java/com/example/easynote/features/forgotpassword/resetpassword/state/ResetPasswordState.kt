package com.example.easynote.features.forgotpassword.resetpassword.state

sealed class ResetPasswordState {
    object Loading : ResetPasswordState()
    object Finished : ResetPasswordState()
    object Success : ResetPasswordState()
    class Error(ex: Throwable) : ResetPasswordState()
}
