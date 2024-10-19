package ru.mavrinvladislav.testtask2024.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import ru.mavrinvladislav.testtask2024.R
import ru.mavrinvladislav.testtask2024.databinding.NoteItemBinding
import ru.mavrinvladislav.testtask2024.domain.Note

class NotesAdapter(
    private val context: Context
) : ListAdapter<Note, NoteViewHolder>(NoteItemDiffCallback()) {

    var onNoteClickListener: ((Note) -> Unit)? = null

    var onNoteLongClickListener: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val colorResId = if (viewType == DRAFT_TYPE) {
            R.color.beige
        } else {
            R.color.white
        }
        val color = ContextCompat.getColor(context, colorResId)
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.container.setBackgroundColor(color)
        return NoteViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isDraft) {
            DRAFT_TYPE
        } else {
            NOT_DRAFT_TYPE
        }
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

    companion object {
        private const val DRAFT_TYPE = 100
        private const val NOT_DRAFT_TYPE = 110

    }
}