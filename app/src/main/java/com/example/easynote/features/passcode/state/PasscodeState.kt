package com.example.easynote.features.passcode.state

sealed class PasscodeState {
    object Loading : PasscodeState()
    object Finished : PasscodeState()
    object OnSuccess : PasscodeState()
}
