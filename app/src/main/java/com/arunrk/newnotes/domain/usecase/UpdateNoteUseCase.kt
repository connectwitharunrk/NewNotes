package com.arunrk.newnotes.domain.usecase

import com.arunrk.newnotes.domain.model.Note
import com.arunrk.newnotes.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Unit> {
        return try {
            if (note.title.isBlank()) {
                Result.failure(Exception("Title cannot be empty"))
            } else {
                repository.updateNote(note)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}