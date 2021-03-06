package com.example.easynote.features.passcode

import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasscodeActivity : BaseActivity() {

    private val viewModel: PasscodeViewModel by viewModel()
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        PasscodeContent(this, viewModel)
    }

}
