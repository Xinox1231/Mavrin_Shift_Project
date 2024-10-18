package ru.mavrinvladislav.testtask2024.domain

data class Note(
    val title: String = EMPTY_STRING,
    val text: String = EMPTY_STRING,
    val id: Int = UNDEFINED_ID,
) {
    companion object {
        private const val EMPTY_STRING = ""
        const val UNDEFINED_ID = 0
    }
}