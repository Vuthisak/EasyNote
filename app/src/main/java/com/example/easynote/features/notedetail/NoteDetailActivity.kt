package com.example.easynote.features.notedetail

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.easynote.BaseActivity
import com.example.easynote.entity.Note
import org.koin.android.ext.android.inject

class NoteDetailActivity : BaseActivity() {

    private val viewModel: NoteDetailViewModel by inject()
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = (intent.extras?.getSerializable(EXTRA_NOTE) as? Note) ?: Note()
    }

    @Composable
    override fun Content() {
        val state = viewModel.state.observeAsState().value
        NoteDetailContent(state, note, onSuccess = {
            setResult(RESULT_OK)
            finish()
        }, onCreateOrUpdate = {
            viewModel.createOrUpdate(note)
        })
    }

    companion object {
        const val EXTRA_NOTE = "NOTE"
    }

}
