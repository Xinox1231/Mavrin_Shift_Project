package ru.mavrinvladislav.testtask2024.data

import ru.mavrinvladislav.testtask2024.data.db.NoteDb
import ru.mavrinvladislav.testtask2024.domain.Note

class NoteMapper {

    fun mapDbNoteToDomain(db: NoteDb): Note = Note(
        title = db.title,
        text = db.text,
        id = db.id
    )

    fun mapDomainNoteToDb(domain: Note): NoteDb = NoteDb(
        title = domain.title,
        text = domain.text,
        id = domain.id
    )


}