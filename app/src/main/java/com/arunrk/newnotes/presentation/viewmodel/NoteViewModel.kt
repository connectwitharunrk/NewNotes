package com.arunrk.newnotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arunrk.newnotes.domain.model.Note
import com.arunrk.newnotes.domain.usecase.AddNoteUseCase
import com.arunrk.newnotes.domain.usecase.DeleteNoteUseCase
import com.arunrk.newnotes.domain.usecase.GetNotesUseCase
import com.arunrk.newnotes.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state: StateFlow<NoteState> = _state.asStateFlow()

    init {
        processIntent(NoteIntent.LoadNotes)
    }

    fun processIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.LoadNotes -> loadNotes()
            is NoteIntent.AddNote -> addNote(intent.title, intent.description)
            is NoteIntent.UpdateNote -> updateNote(intent.note)
            is NoteIntent.DeleteNote -> deleteNote(intent.note)
            is NoteIntent.SelectNote -> selectNote(intent.note)
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getNotesUseCase()
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
                .collect { notes ->
                    _state.update {
                        it.copy(notes = notes, isLoading = false, error = null)
                    }
                }
        }
    }

    private fun addNote(title: String, description: String) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                description = description,
                timestamp = System.currentTimeMillis()
            )
            addNoteUseCase(note)
                .onSuccess {
                    _state.update { it.copy(error = null) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) }
                }
        }
    }

    private fun updateNote(note: Note) {
        viewModelScope.launch {
            val updatedNote = note.copy(timestamp = System.currentTimeMillis())
            updateNoteUseCase(updatedNote)
                .onSuccess {
                    _state.update { it.copy(selectedNote = null, error = null) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) }
                }
        }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase(note)
                .onSuccess {
                    _state.update { it.copy(error = null) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) }
                }
        }
    }

    private fun selectNote(note: Note?) {
        _state.update { it.copy(selectedNote = note) }
    }
}