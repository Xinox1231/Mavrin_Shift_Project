package ru.mavrinvladislav.testtask2024.data

import kotlinx.coroutines.flow.Flow
import ru.mavrinvladislav.testtask2024.data.db.NoteDb
import ru.mavrinvladislav.testtask2024.data.db.NotesDao
import javax.inject.Inject

class NoteRoomLocalDataSource @Inject constructor(
    private val notesDao: NotesDao
) : NoteLocalDataSource {
    override suspend fun addNote(noteDb: NoteDb) {
        notesDao.addNote(noteDb)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override fun getNotesList(): Flow<List<NoteDb>> {
        return notesDao.getNotesList()
    }

    override fun searchNote(query: String): Flow<List<NoteDb>> {
        return notesDao.searchNotes(query)
    }

    override suspend fun getNote(noteId: Int): NoteDb {
        return notesDao.getNote(noteId)
    }
}