package com.example.teacherassistant2

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StudentDetails2Activity : AppCompatActivity() {
    private var studentId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_details2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = findViewById<TextView>(R.id.name)
        val album = findViewById<TextView>(R.id.album)
        val subjects = findViewById<TextView>(R.id.subjects)

        intent.extras?.let { bundle ->
            studentId = bundle.getInt("studentId", 0)
            if (studentId != 0) {
                name.text = bundle.getString("studentName", "") + " " +bundle.getString("studentSurname", "")
                album.text = bundle.getInt("studentAlbum", 0).toString()
            }
        }
    }
}