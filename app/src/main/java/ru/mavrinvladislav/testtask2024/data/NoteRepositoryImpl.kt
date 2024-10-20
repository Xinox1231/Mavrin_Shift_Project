package ru.mavrinvladislav.testtask2024.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.domain.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val localDataSource: NoteLocalDataSource,
    private val mapper: NoteMapper
) : NoteRepository {
    override suspend fun createNewNote(
        isDraft: Boolean,
        title: String,
        text: String
    ): Note {
        val note = Note(isDraft, title, text)
        return note
    }

    override suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            localDataSource.addNote(mapper.mapDomainNoteToDb(note))
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return localDataSource.getNotesList()
            .map { list -> list.map { mapper.mapDbNoteToDomain(it) } }
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return localDataSource.searchNote(query)
            .map { list -> list.map { mapper.mapDbNoteToDomain(it) } }
    }

    override suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteNote(note.id)
        }
    }

    override suspend fun getNote(noteId: Int): Note {
        return withContext(Dispatchers.IO) {
            val dbModel = localDataSource.getNote(noteId)
            mapper.mapDbNoteToDomain(dbModel)
        }
    }
}