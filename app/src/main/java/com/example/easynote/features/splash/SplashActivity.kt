package com.example.easynote.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import com.example.easynote.features.login.LoginActivity
import com.example.easynote.features.main.MainActivity
import com.example.easynote.features.passcode.PasscodeActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    override val content: BaseContent = SplashContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goNextScreen()
    }

    private fun goNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseAuth.currentUser == null) {
                FirebaseAuth.getInstance().signOut()
                gotoLoginScreen()
            } else if (preferences.getPasscode().isBlank()) {
                gotoPasscodeScreen()
            } else {
                gotoMainScreen()
            }
        }, SPLASH_DISPLAY_LENGTH)
        finish()
    }

    private fun gotoLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun gotoPasscodeScreen() {
        val intent = Intent(this, PasscodeActivity::class.java)
        startActivity(intent)
    }

    private fun gotoMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 1000L
    }

}
