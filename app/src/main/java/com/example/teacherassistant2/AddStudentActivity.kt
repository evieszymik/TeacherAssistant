package com.example.teacherassistant2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddStudentActivity : AppCompatActivity() {
    private var studentId: Int = 0
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameInput = findViewById<EditText>(R.id.edit_text_name)
        val surnameInput = findViewById<EditText>(R.id.edit_text_surname)
        val albumInput = findViewById<EditText>(R.id.edit_text_album)
        val addButton = findViewById<Button>(R.id.btn_addStudent)

        addButton.setOnClickListener{
            val name =nameInput.text.toString()
            val surname =surnameInput.text.toString()
            val album =albumInput.text.toString()

            //val grades: MutableList<Double> = mutableListOf()
            val grades: Array<Double> = arrayOf()

            if (name.isNotBlank() && surname.isNotBlank() && album.isNotBlank()) {
                val student = Student(
                    idStudent = if (isEditMode) studentId else 0,
                    name = name,
                    surname = surname,
                    album = album.toInt()
                )

                lifecycleScope.launch {
                    AppDatabase.getInstance(this@AddStudentActivity)
                        .studentDao()
                        .insert(student)
                    runOnUiThread {
                        Toast.makeText(
                            this@AddStudentActivity,
                            if (isEditMode) "Uczestnik zaktualizowany" else "Student added",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
                startActivity(Intent(this, StudentListActivity::class.java))
            } else {
                Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }
    }
}