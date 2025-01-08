package com.example.teacherassistant2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Query("SELECT * FROM student")
    suspend fun getAll(): List<Student>

    @Query("SELECT * FROM student WHERE idStudent NOT IN (SELECT idStudent FROM enrollment WHERE idSubject = :idSubject)")
    suspend fun getRest(idSubject: Int): List<Student>

    @Query("SELECT * FROM student WHERE idStudent = :idStudent")
    suspend fun getById(idStudent: Int): Student?

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("DELETE FROM student")
    suspend fun deleteAll()
}
