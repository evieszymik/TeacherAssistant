package com.example.teacherassistant2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: Subject)

    @Query("SELECT * FROM subject")
    suspend fun getAll(): List<Subject>

    @Query("SELECT * FROM subject WHERE idSubject = :idSubject")
    suspend fun getById(idSubject: Int): Subject?

    @Update
    suspend fun update(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)

    @Query("DELETE FROM subject")
    suspend fun deleteAll()
}
