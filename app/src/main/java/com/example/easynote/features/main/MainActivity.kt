package com.example.easynote.features.main

import android.os.Bundle
import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        MainContent(this, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reload()
    }

    private fun reload(isReloaded: Boolean = false) {
        viewModel.getNotes(isReloaded)
    }

}
