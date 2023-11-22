package com.example.lesson_8_fokin.presentation

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_8_fokin.data.entity.NoteEntity
import com.example.lesson_8_fokin.model.NoteListener

class NoteAdapter : RecyclerView.Adapter<NoteViewHolder>() {
    lateinit var onClickListener: NoteListener
    lateinit var onLongClickListener: NoteListener
    private val notes = mutableListOf<NoteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(parent, onClickListener, onLongClickListener)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(notes: List<NoteEntity>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }
}