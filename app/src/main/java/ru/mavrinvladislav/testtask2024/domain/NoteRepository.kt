package ru.mavrinvladislav.testtask2024.domain

import kotlinx.coroutines.flow.Flow


interface NoteRepository {

    suspend fun createNewNote(
        isDraft: Boolean,
        title: String,
        text: String
    ): Note

    suspend fun saveNote(
        note: Note
    )

    fun getAllNotes(): Flow<List<Note>>

    suspend fun deleteNote(
        note: Note
    )

    suspend fun getNote(
        noteId: Int
    ): Note
}