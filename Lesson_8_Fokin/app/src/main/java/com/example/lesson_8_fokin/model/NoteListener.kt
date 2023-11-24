package com.example.lesson_8_fokin.model

import com.example.lesson_8_fokin.data.entity.NoteEntity

interface NoteListener {
    fun onClick(item: NoteEntity)
    fun onLongClick(item: NoteEntity)
}