package ru.mavrinvladislav.testtask2024.domain

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId)
    }
}