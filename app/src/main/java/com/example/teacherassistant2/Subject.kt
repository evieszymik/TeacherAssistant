package com.example.teacherassistant2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject")
data class Subject(
    @PrimaryKey(autoGenerate = true) val idSubject: Int = 0,
    val name: String,
    val weekday: String,
    val time: String
)