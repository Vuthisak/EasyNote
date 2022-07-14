package com.example.easynote.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.easynote.ui.theme.EasyNoteTheme

abstract class BaseActivity : ComponentActivity() {

    abstract val content: BaseContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EasyNoteTheme { content.register() } }
    }

    protected fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }

}
