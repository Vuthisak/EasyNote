package com.example.easynote.features.notedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.entity.Note
import com.example.easynote.repository.NoteRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private var _state = MutableLiveData<NoteDetailState>()
    val state = _state

    fun createOrUpdate(note: Note) {
        if (!note.id.isNullOrBlank()) {
            updateNote(note)
        } else {
            createNote(note)
        }
    }

    private fun createNote(note: Note) = viewModelScope.launch {
        repository.saveNote(note)
            .onStart {
                _state.value = NoteDetailState.Loading
            }.catch { cause ->
                _state.value = NoteDetailState.Error(cause)
            }.mapLatest {
                _state.value = NoteDetailState.UpdateOrSaveSuccess
            }.stateIn(scope = this)
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
            .onStart {
                _state.value = NoteDetailState.Loading
            }.catch { cause ->
                _state.value = NoteDetailState.Error(cause)
            }.mapLatest {
                _state.value = NoteDetailState.UpdateOrSaveSuccess
            }.stateIn(scope = this)
    }

}