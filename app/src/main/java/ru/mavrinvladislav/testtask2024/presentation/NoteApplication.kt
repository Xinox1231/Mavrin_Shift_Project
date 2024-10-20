package ru.mavrinvladislav.testtask2024.presentation

import android.app.Application
import ru.mavrinvladislav.testtask2024.di.DaggerApplicationComponent

class NoteApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}