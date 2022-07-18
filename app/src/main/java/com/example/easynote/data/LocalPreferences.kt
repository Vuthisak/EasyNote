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
        preferences.edit().putString(KEY_PASSCODE, passcode).apply()
    }

    override fun getPasscode(): String? = preferences.getString(KEY_PASSCODE, null)

    private companion object {
        private const val KEY_PASSCODE = "KEY_PASSWORD"
    }

}
