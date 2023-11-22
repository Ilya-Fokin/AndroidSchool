package com.example.lesson_8_fokin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lesson_8_fokin.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun save(note: NoteEntity)

    @Query("select * from ${NoteEntity.TABLE_NAME} where inArchive = 0")
    fun getNotesFlow(): Flow<List<NoteEntity>>

    @Query("select * from ${NoteEntity.TABLE_NAME} where title like '%' || :str || '%' or description like '%' || :str || '%' and inArchive = 0")
    fun searchNotesFlowByName(str: String): Flow<List<NoteEntity>>

    @Query("update ${NoteEntity.TABLE_NAME} set inArchive = 1 where id = :id")
    suspend fun addInArchive(id: String)

    @Update
    suspend fun update(note: NoteEntity)

    @Query("delete from ${NoteEntity.TABLE_NAME} where id = :id")
    suspend fun deleteById(id: String)
}