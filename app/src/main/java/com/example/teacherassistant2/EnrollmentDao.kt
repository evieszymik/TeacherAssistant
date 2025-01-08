package com.example.teacherassistant2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EnrollmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(enrollment: Enrollment)

    @Query("SELECT * FROM enrollment WHERE idStudent = :idStudent AND idSubject = :idSubject")
    suspend fun getByStudentAndSubject(idStudent: Int, idSubject: Int): Enrollment?

    @Query("SELECT * FROM enrollment WHERE idStudent = :idStudent")
    suspend fun getByStudentId(idStudent: Int): List<Enrollment>

    @Delete
    suspend fun delete(enrollment: Enrollment)

    @Query("DELETE FROM enrollment WHERE idStudent= :idStudent")
    suspend fun deleteByStudentId(idStudent: Int)

    @Query("DELETE FROM enrollment WHERE idSubject = :idSubject")
    suspend fun deleteBySubjectId(idSubject: Int)

    @Query("SELECT * FROM student INNER JOIN enrollment ON student.idStudent = enrollment.idStudent WHERE enrollment.idSubject = :idSubject")
    suspend fun getStudentsForSubject(idSubject: Int): List<Student>

    @Query("DELETE FROM enrollment")
    suspend fun deleteAll()
}

