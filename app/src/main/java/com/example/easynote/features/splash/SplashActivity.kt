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
                gotoLoginScreen()
            } else {
                gotoMainScreen()
            }
        }, SPLASH_DISPLAY_LENGTH)
    }

    private fun gotoLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 1000L
    }

}
