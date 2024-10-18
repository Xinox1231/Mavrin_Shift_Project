package ru.mavrinvladislav.testtask2024.domain

class CreateNewNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(title: String, text: String): Note {
        return repository.createNewNote(title, text)
    }
}