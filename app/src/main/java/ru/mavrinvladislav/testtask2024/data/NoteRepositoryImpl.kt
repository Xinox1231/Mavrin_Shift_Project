package ru.mavrinvladislav.testtask2024.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.domain.NoteRepository

class NoteRepositoryImpl(
    private val localDataSource: NoteLocalDataSource,
    private val mapper: NoteMapper
) : NoteRepository {
    override suspend fun createNewNote(
        title: String,
        text: String
    ): Note {
        val note = Note(title, text)
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