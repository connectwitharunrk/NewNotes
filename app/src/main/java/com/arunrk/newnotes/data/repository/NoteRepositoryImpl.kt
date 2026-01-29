package com.arunrk.newnotes.data.repository

import com.arunrk.newnotes.data.local.NoteDao
import com.arunrk.newnotes.data.local.NoteEntity
import com.arunrk.newnotes.domain.model.Note
import com.arunrk.newnotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
): NoteRepository {

    override suspend fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)?.toDomainModel()
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }

    private fun NoteEntity.toDomainModel() = Note(
        id = this.id,
        title = this.title,
        description = this.description,
        timestamp = this.timestamp
    )

    private fun Note.toEntity() = NoteEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        timestamp = this.timestamp
    )

}