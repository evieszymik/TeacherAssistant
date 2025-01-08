package com.example.teacherassistant2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class Student(
    @PrimaryKey(autoGenerate = true) val idStudent: Int = 0,
    val name: String,
    val surname: String,
    val album: Int
)