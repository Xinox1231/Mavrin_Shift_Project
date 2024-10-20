package ru.mavrinvladislav.testtask2024.domain

import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}