package com.basitbhatti.notesapp.supabase.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basitbhatti.notesapp.supabase.data.model.Note
import com.basitbhatti.notesapp.supabase.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val repo = NotesRepository()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            runCatching { repo.getNotes() }
                .onSuccess {
                    _notes.value = it
                }
                .onFailure {
                    _error.value = it.message
                }
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            runCatching { repo.addNote(title, content) }
                .onSuccess { loadNotes() }
                .onFailure { _error.value = it.message }
        }
    }


    fun deleteNote(id: String) {
        viewModelScope.launch {
            runCatching { repo.deleteNote(id) }
                .onSuccess { loadNotes() }
                .onFailure { _error.value = it.message }
        }
    }


}