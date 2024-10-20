package ru.mavrinvladislav.testtask2024.domain

import javax.inject.Inject

class CreateNewNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(
        isDraft: Boolean,
        title: String,
        text: String
    ): Note {
        return repository.createNewNote(isDraft, title, text)
    }
}