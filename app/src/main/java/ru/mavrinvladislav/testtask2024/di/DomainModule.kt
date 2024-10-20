package ru.mavrinvladislav.testtask2024.di

import dagger.Binds
import dagger.Module
import ru.mavrinvladislav.testtask2024.data.NoteRepositoryImpl
import ru.mavrinvladislav.testtask2024.domain.NoteRepository

@Module
interface DomainModule {

    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
}