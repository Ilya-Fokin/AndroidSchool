package com.example.lesson_8_fokin.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = NoteEntity.TABLE_NAME)
@Parcelize
class NoteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    var color: Int,
    val inArchive: Boolean = false
) : Parcelable {

    companion object {
        const val TABLE_NAME = "notes"
    }
}