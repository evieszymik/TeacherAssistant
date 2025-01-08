package com.example.teacherassistant2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        findViewById<TextView>(R.id.name).text="$studentName $studentSurname"
        findViewById<TextView>(R.id.subject).text=intent.getStringExtra("subjectName") ?: ""

        findViewById<Button>(R.id.btn_addGrade).setOnClickListener{

        }
    }
}