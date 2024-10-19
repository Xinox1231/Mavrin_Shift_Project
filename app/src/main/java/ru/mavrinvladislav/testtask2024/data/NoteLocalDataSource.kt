package ru.mavrinvladislav.testtask2024.data

import kotlinx.coroutines.flow.Flow
import ru.mavrinvladislav.testtask2024.data.db.NoteDb
import ru.mavrinvladislav.testtask2024.domain.Note

interface NoteLocalDataSource {

    suspend fun addNote(noteDb: NoteDb)

    suspend fun deleteNote(noteId: Int)

    fun getNotesList(): Flow<List<NoteDb>>

    fun searchNote(query: String): Flow<List<NoteDb>>
    suspend fun getNote(noteId: Int): NoteDb
}