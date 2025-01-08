package com.example.teacherassistant2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddSubjectActivity : AppCompatActivity() {
    private var subjectId: Int = 0
    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_subject)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner: Spinner = findViewById(R.id.spinnerOptions)
        val options = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val nameInput = findViewById<EditText>(R.id.edit_text_name)
        val hourInput = findViewById<EditText>(R.id.edit_text_time)
        val addButton = findViewById<Button>(R.id.btn_addSubject)


        intent.extras?.let { bundle ->
            subjectId = bundle.getInt("subjectId", 0)
            if (subjectId != 0) {
                isEditMode = true
                nameInput.setText(bundle.getString("subjectName", ""))
                hourInput.setText(bundle.getString("subjectHour", "11"))
                spinner.setSelection(options.indexOf(bundle.getString("subjectWeekday", "")))
                addButton.text = "Save changes"
            }
        }

        addButton.setOnClickListener{
            val name =nameInput.text.toString()
            val day =spinner.selectedItem.toString()
            val hour =hourInput.text.toString()

            if (name.isNotBlank() && day.isNotBlank() && hour.isNotBlank()) {
                val subject = Subject(
                    idSubject = if (isEditMode) subjectId else 0,
                    name = name,
                    weekday = day,
                    time = hour
                )

                lifecycleScope.launch {
                    AppDatabase.getInstance(this@AddSubjectActivity)
                        .subjectDao()
                        .insert(subject)
                    runOnUiThread {
                        Toast.makeText(
                            this@AddSubjectActivity,
                            if (isEditMode) "Subject edited" else "Subject added",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
                startActivity(Intent(this, SubjectListActivity::class.java))
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}