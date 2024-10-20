package ru.mavrinvladislav.testtask2024.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_notes")
data class NoteDb(
    val isDraft: Boolean,
    val title: String,
    val text: String,
    val isPinned: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = UNDEFINED_ID,
) {
    companion object {
        private const val UNDEFINED_ID = 0
        private val EMPTY_LIST = emptyList<String>()
    }
}