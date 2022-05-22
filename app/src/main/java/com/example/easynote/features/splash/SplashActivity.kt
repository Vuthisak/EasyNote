package com.example.easynote.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import com.example.easynote.BaseActivity
import com.example.easynote.features.main.MainActivity
import com.example.easynote.features.splash.components.SplashContent

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goNextScreen()
    }

    @Composable
    override fun Content() = SplashContent()

    private fun goNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            openMainScreen()
        }, SPLASH_DISPLAY_LENGTH)
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 1000L
    }

}
