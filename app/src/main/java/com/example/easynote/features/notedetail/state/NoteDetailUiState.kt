package com.example.easynote.features.notedetail.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.easynote.base.BaseUiState
import com.example.easynote.entity.Note
import com.example.easynote.util.getOrDefault

data class NoteDetailUiState(
    val titleState: MutableState<String> = mutableStateOf(""),
    val descState: MutableState<String> = mutableStateOf("")
) : BaseUiState() {

    constructor(note: Note) : this() {
        titleState.value = note.title.getOrDefault()
        descState.value = note.desc.getOrDefault()
    }

}
