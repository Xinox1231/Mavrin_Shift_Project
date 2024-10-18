package ru.mavrinvladislav.testtask2024.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDb: NoteDb)

    @Query("DELETE FROM all_notes WHERE id=:noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM all_notes")
    fun getNotesList(): Flow<List<NoteDb>>

    @Query("SELECT * FROM all_notes WHERE id=:noteId LIMIT 1")
    suspend fun getNote(noteId: Int): NoteDb
}