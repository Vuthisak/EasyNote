package com.example.easynote.features.main

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.easynote.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reload()
    }

    @Composable
    override fun Content() = MainContent(lifecycleScope, this, viewModel)

    private fun reload(isReloaded: Boolean = false) {
        viewModel.getNotes(isReloaded)
    }

}
