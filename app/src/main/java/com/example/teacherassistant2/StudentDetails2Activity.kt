package com.example.teacherassistant2

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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
        var items=""

        intent.extras?.let { bundle ->
            studentId = bundle.getInt("studentId", 0)
            if (studentId != 0) {
                name.text = bundle.getString("studentName", "") + " " +bundle.getString("studentSurname", "")
                album.text = bundle.getInt("studentAlbum", 0).toString()

                lifecycleScope.launch {
                    val db = AppDatabase.getInstance(this@StudentDetails2Activity)
                    val subjectsList = db.enrollmentDao().getByStudentId(studentId)
                    for(el in subjectsList){
                        val subject = db.subjectDao().getById(el.idSubject)
                        items+=subject?.name+" \n"
                    }
                    subjects.text=items
                }
            }
        }
    }
}