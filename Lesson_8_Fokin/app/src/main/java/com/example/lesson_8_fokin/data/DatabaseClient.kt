package com.example.lesson_8_fokin.data

import android.content.Context
import androidx.room.Room
import com.example.lesson_8_fokin.data.dao.NoteDao

object DatabaseClient {

    private var db: AppDatabase? = null

    fun noteDao(context: Context): NoteDao {
        return getInstance(context).noteDao()
    }

    private fun getInstance(context: Context): AppDatabase {
        return db ?: run {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
                .build()
            this.db = db
            db
        }
    }
}