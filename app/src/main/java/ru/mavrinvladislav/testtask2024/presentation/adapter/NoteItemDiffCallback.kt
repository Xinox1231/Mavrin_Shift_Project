package ru.mavrinvladislav.testtask2024.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.mavrinvladislav.testtask2024.domain.Note

class NoteItemDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}