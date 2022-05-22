package com.example.easynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.easynote.ui.theme.EasyNoteTheme

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyNoteTheme {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()

}
