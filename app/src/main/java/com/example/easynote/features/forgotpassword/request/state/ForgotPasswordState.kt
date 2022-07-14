package com.example.easynote.features.forgotpassword.request.state

sealed class ForgotPasswordState {
    object Loading : ForgotPasswordState()
    object Finished : ForgotPasswordState()
}
