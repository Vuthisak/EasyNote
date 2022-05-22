package com.example.easynote.features.main

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.easynote.BaseActivity
import com.example.easynote.features.notedetail.NoteDetailActivity
import com.example.easynote.util.getOrDefault
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reload()
    }

    @Composable
    override fun Content() {
        val state = viewModel.state.collectAsState().value
        val intent = Intent(this, NoteDetailActivity::class.java)
        MainContent(state, intent, onDelete = {
            viewModel.removeNote(it.id.getOrDefault())
        }, onResultOk = {
            reload(true)
        })
    }

    private fun reload(isReloaded: Boolean = false) {
        viewModel.getNotes(isReloaded)
    }

}
