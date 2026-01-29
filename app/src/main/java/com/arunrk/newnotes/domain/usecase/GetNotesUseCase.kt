package com.arunrk.newnotes.domain.usecase

import com.arunrk.newnotes.domain.model.Note
import com.arunrk.newnotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}