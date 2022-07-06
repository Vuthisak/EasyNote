package com.example.easynote.features.notedetail

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.easynote.base.BaseActivity
import com.example.easynote.entity.Note
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteDetailActivity : BaseActivity() {

    private val viewModel: NoteDetailViewModel by viewModel()
    private val firebase = FirebaseAuth.getInstance()
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = (intent.extras?.getSerializable(EXTRA_NOTE) as? Note) ?: Note()
    }

    @Composable
    override fun Content() {
        NoteDetailContent(lifecycleScope, viewModel, note, onSuccess = {
            setResult(RESULT_OK)
            finish()
        }, onCreateOrUpdate = {
            it.userId = firebase.currentUser?.uid
            viewModel.createOrUpdate(note)
        })
    }

    companion object {
        const val EXTRA_NOTE = "NOTE"
    }

}
