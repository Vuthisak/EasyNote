package com.example.easynote.data

import android.content.Context
import com.securepreferences.SecurePreferences

interface LocalPreferences {
    fun savePasscode(passcode: String)
    fun getPasscode(): String?
}

class LocalPreferencesImpl(
    context: Context
) : LocalPreferences {

    private val preferences = SecurePreferences(context)

    override fun savePasscode(passcode: String) {

    }

    override fun getPasscode(): String? {
        val encodedPassword = preferences.getString(KEY_PASSCODE, null)
        if (encodedPassword != null) {

        }
        return null
    }

    private companion object {
        private const val KEY_PASSCODE = "KEY_PASSWORD"
    }

}
