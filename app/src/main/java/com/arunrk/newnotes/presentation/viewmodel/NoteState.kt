package com.arunrk.newnotes.presentation.viewmodel

import com.arunrk.newnotes.domain.model.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val selectedNote: Note? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)