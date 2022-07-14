package com.example.easynote.features.notedetail

import android.os.Bundle
import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import com.example.easynote.entity.Note
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteDetailActivity : BaseActivity() {

    private val viewModel: NoteDetailViewModel by viewModel()
    private lateinit var note: Note
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        NoteDetailContent(this, viewModel, note)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        note = (intent.extras?.getSerializable(EXTRA_NOTE) as? Note) ?: Note()
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val EXTRA_NOTE = "NOTE"
    }

}
