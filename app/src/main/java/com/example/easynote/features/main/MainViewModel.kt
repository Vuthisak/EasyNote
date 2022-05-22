package com.example.easynote.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
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

    fun removeNote(noteId: String) = viewModelScope.launch(Dispatchers.Main) {
        noteRepository.removeNote(noteId)
            .catch { cause ->
                _state.value = MainState.Error(cause)
            }.stateIn(scope = this)
    }

    fun getNotes(isReloaded: Boolean = false) = viewModelScope.launch(Dispatchers.Main) {
        noteRepository.getNotes()
            .stateIn(scope = this)
            .onStart {
                _state.value = MainState.Loading
            }.catch { cause ->
                _state.value = MainState.Error(cause)
            }.onCompletion {
                _state.value = MainState.Finished
            }
            .collect { notes ->
                _state.value = MainState.OnGetListSuccess(notes, isReloaded)
            }
    }

}
