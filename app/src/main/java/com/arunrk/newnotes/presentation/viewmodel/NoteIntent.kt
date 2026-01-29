package com.arunrk.newnotes.presentation.viewmodel

import com.arunrk.newnotes.domain.model.Note

sealed class NoteIntent {
    data object LoadNotes : NoteIntent()
    data class AddNote(val title: String, val description: String) : NoteIntent()
    data class UpdateNote(val note: Note) : NoteIntent()
    data class DeleteNote(val note: Note) : NoteIntent()
    data class SelectNote(val note: Note?) : NoteIntent()
}