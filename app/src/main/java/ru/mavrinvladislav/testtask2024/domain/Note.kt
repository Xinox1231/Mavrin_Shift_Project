package ru.mavrinvladislav.testtask2024.domain

data class Note(
    val isDraft: Boolean,
    val title: String,
    val text: String,
    val id: Int = UNDEFINED_ID,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}