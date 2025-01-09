package com.example.teacherassistant2

import androidx.room.Entity

@Entity(
    tableName = "enrollment",
    primaryKeys = ["idStudent", "idSubject"]
)
data class Enrollment(
    val idStudent: Int,
    val idSubject: Int,
    val grades: String = ""
)