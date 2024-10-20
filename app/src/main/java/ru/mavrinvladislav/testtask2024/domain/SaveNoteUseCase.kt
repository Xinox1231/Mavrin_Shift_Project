package ru.mavrinvladislav.testtask2024.domain

import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.saveNote(note)
    }
}