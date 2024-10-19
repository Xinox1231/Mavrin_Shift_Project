package ru.mavrinvladislav.testtask2024.domain

import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNote(query)
    }
}