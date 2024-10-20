package ru.mavrinvladislav.testtask2024.domain

import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId)
    }
}