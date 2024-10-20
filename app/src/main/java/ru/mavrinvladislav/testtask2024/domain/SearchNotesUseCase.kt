package ru.mavrinvladislav.testtask2024.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNote(query)
    }
}