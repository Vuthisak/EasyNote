package com.example.easynote.features.passcode

import androidx.compose.runtime.Composable
import com.example.easynote.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasscodeActivity : BaseActivity() {

    private val viewModel: PasscodeViewModel by viewModel()
    private val passwordContent by lazy { PasswordContent(this, viewModel) }

    @Composable
    override fun Content() = passwordContent.get()

}
