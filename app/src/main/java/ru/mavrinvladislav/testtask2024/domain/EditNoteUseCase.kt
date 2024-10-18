package ru.mavrinvladislav.testtask2024.domain

class EditNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.saveNote(note)
    }
}