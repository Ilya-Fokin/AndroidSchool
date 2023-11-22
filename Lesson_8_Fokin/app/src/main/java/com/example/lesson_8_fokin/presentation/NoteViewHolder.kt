package com.example.lesson_8_fokin.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_8_fokin.R
import com.example.lesson_8_fokin.databinding.ItemNoteBinding
import com.example.lesson_8_fokin.data.entity.NoteEntity
import com.example.lesson_8_fokin.model.NoteListener

class NoteViewHolder(
    private val parent: ViewGroup,
    private val onClickListener: NoteListener,
    private val onLongClickListener: NoteListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
) {
    private val binding by viewBinding(ItemNoteBinding::bind)

    fun bind(item: NoteEntity) = with(binding) {
        root.setOnClickListener {
            onClickListener.onClick(item)
        }
        root.setOnLongClickListener {
            onLongClickListener.onClick(item)
            true
        }
        if (item.color == R.color.white) {
            textViewTitle.setTextColor(ContextCompat.getColor(root.context, R.color.black))
            textViewDescription.setTextColor(ContextCompat.getColor(root.context, R.color.black))
        } else {
            textViewTitle.setTextColor(ContextCompat.getColor(root.context, R.color.white))
            textViewDescription.setTextColor(ContextCompat.getColor(root.context, R.color.white))
        }
        textViewTitle.text = item.title
        textViewDescription.text = item.description
        linearLayoutCard.setBackgroundResource(item.color)
    }
}