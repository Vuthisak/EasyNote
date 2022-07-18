package com.example.easynote.features.forgotpassword.resetpassword

import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : BaseActivity() {

    private val viewModel: ResetPasswordViewModel by viewModel()
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        ResetPasswordContent(this, viewModel)
    }

}
