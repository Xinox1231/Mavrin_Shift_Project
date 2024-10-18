package ru.mavrinvladislav.testtask2024.data

import kotlinx.coroutines.flow.Flow
import ru.mavrinvladislav.testtask2024.data.db.NoteDb
import ru.mavrinvladislav.testtask2024.data.db.NotesDao

class NoteRoomLocalDataSource(
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

    override suspend fun getNote(noteId: Int): NoteDb {
        return notesDao.getNote(noteId)
    }
}