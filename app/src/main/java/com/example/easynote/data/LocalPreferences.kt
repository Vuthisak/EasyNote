package com.example.easynote.data

import android.content.Context
import com.example.easynote.util.getOrDefault
import com.securepreferences.SecurePreferences

interface LocalPreferences {
    fun savePasscode(passcode: String)
    fun getPasscode(): String
    fun removePasscode()
}

class LocalPreferencesImpl(
    context: Context
) : LocalPreferences {

    private val preferences = SecurePreferences(context)

    override fun removePasscode() {
        preferences.edit().remove(KEY_PASSCODE).apply()
    }

    override fun savePasscode(passcode: String) {
        preferences.edit().putString(KEY_PASSCODE, passcode).apply()
    }

    override fun getPasscode(): String = preferences.getString(KEY_PASSCODE, "").getOrDefault()

    private companion object {
        private const val KEY_PASSCODE = "KEY_PASSWORD"
    }

}
