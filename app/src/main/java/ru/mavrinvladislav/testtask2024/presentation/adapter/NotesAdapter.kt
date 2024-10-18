package ru.mavrinvladislav.testtask2024.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.mavrinvladislav.testtask2024.databinding.NoteItemBinding
import ru.mavrinvladislav.testtask2024.domain.Note

class NotesAdapter : ListAdapter<Note, NoteViewHolder>(NoteItemDiffCallback()) {

    var onNoteClickListener: ((Note) -> Unit)? = null

    var onNoteLongClickListener: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvTitle.text = item.title
            tvText.text = item.text

            root.setOnClickListener {
                onNoteClickListener?.invoke(item)
            }
            root.setOnLongClickListener {
                onNoteLongClickListener?.invoke(item)
                true
            }
        }

    }
}