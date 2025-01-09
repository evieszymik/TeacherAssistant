package com.example.teacherassistant2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddGradeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_grade)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner: Spinner = findViewById(R.id.spinnerOptions)
        val options = listOf("2", "2.5", "3", "3.5", "4", "4.5", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        val studentName = intent.getStringExtra("studentName") ?: ""
        val studentSurname = intent.getStringExtra("studentSurname") ?: ""
        val studentId = intent.getIntExtra("studentId", 0)
        val subjectId = intent.getIntExtra("subjectId", 0)
        findViewById<TextView>(R.id.name).text="$studentName $studentSurname"
        findViewById<TextView>(R.id.subject).text=intent.getStringExtra("subjectName") ?: ""


        findViewById<Button>(R.id.btn_addGrade).setOnClickListener{
            val points = findViewById<EditText>(R.id.points).text.toString()
            val grade = spinner.selectedItem
            var gradeList = ""
            val gradeFinal: String = points.ifBlank {
                grade.toString()
            }
            lifecycleScope.launch {
                val item = AppDatabase.getInstance(this@AddGradeActivity)
                    .enrollmentDao()
                    .getByStudentAndSubject(studentId, subjectId)

                gradeList+=item?.grades
                gradeList+= "$gradeFinal, "

                lifecycleScope.launch {
                    val enrollment = Enrollment(
                        idStudent = studentId,
                        idSubject = subjectId,
                        grades = gradeList
                    )
                    AppDatabase.getInstance(this@AddGradeActivity)
                        .enrollmentDao().insert(enrollment)

                    runOnUiThread {
                        Toast.makeText(
                            this@AddGradeActivity,
                            "Grade added",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
    }
}