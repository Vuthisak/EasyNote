package com.example.easynote.features.passcode

import androidx.lifecycle.ViewModel
import com.example.easynote.data.LocalPreferences

class PasscodeViewModel(
    private val preferences: LocalPreferences
) : ViewModel() {

    fun savePasscode(passcode: String) {
        preferences.savePasscode(passcode)
    }

}
