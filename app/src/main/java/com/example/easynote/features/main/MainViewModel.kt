package com.example.easynote.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var _state = MutableStateFlow<MainState>(MainState.Finished)
    val state = _state.asStateFlow()

    fun finished() {
        _state.value = MainState.Finished
    }

    fun removeNote(noteId: String) = viewModelScope.launch {
        noteRepository.removeNote(noteId)
            .catch { cause ->
                _state.value = MainState.Error(cause)
            }.stateIn(scope = this)
    }

    fun getNotes(isReloaded: Boolean = false) = viewModelScope.launch {
        noteRepository.getNotes()
            .onStart {
                _state.value = MainState.Loading
            }.catch { cause ->
                _state.value = MainState.Error(cause)
            }.mapLatest { notes ->
                _state.value = MainState.OnGetListSuccess(notes, isReloaded)
            }.stateIn(scope = this)
    }

}
