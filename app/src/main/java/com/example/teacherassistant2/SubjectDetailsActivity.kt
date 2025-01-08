package com.example.teacherassistant2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.google.android.material.appbar.MaterialToolbar

class SubjectDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subject_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val subjectId= intent.getIntExtra("subjectId", 0)
        val subjectName = intent.getStringExtra("subjectName") ?: ""
        val subjectWeekday = intent.getStringExtra("subjectWeekday") ?: ""
        val subjectHour = intent.getStringExtra("subjectHour") ?: ""

        val buttonAdd = findViewById<Button>(R.id.addStudent)
        loadSubjectStudents(subjectId, subjectName)
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        toolbar.title = "$subjectName: $subjectWeekday, $subjectHour"

        buttonAdd.setOnClickListener{
            showDialog(subjectName, subjectId)
        }

    }
    private fun showDialog(subjectName: String, subjectId:Int){
        val list = mutableListOf<Pair<String, Int>>()
        lifecycleScope.launch {
            val students = AppDatabase.getInstance(this@SubjectDetailsActivity)
                .studentDao()
                .getRest(subjectId)

            for(student in students){
                list.add(Pair("${student.name} ${student.surname}", student.idStudent))
            }

            val builder = AlertDialog.Builder(this@SubjectDetailsActivity)
            val options = list.map { it.first }.toTypedArray()
            builder.setTitle("Add Student to $subjectName")
                .setItems(options) { _, which ->
                    lifecycleScope.launch {
                        val enrollment = Enrollment(list[which].second,subjectId)
                        AppDatabase.getInstance(this@SubjectDetailsActivity)
                            .enrollmentDao()
                            .insert(enrollment)
                        loadSubjectStudents(subjectId,subjectName)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.setOnShowListener {
                val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                cancelButton.setTextColor(ContextCompat.getColor(this@SubjectDetailsActivity, R.color.white))
                cancelButton.setBackgroundColor(ContextCompat.getColor(this@SubjectDetailsActivity, R.color.red))
            }
            dialog.show()
        }
    }

    private fun loadSubjectStudents(subjectId: Int, subjectName: String) {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@SubjectDetailsActivity)
            val students = db.enrollmentDao().getStudentsForSubject(subjectId)

            runOnUiThread {
                val adapter = object : ArrayAdapter<Student>(
                    this@SubjectDetailsActivity,
                    R.layout.student_item2,
                    students
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = convertView ?: layoutInflater.inflate(R.layout.student_item2, parent, false)
                        val student = getItem(position)!!

                        view.findViewById<TextView>(R.id.text_view_item).text = "${student.name} ${student.surname}"

                        view.setOnClickListener {
                            val intent = Intent(this@SubjectDetailsActivity, StudentDetailsActivity::class.java).apply {
                                putExtra("studentId", student.idStudent)
                                putExtra("studentName", student.name)
                                putExtra("studentSurname", student.surname)
                                putExtra("studentAlbum", student.album)
                                putExtra("subjectName", subjectName)
                            }
                            startActivity(intent)
                        }

                        view.findViewById<Button>(R.id.btn_addGrade).setOnClickListener {
                            val intent = Intent(this@SubjectDetailsActivity, AddGradeActivity::class.java).apply {
                                putExtra("studentId", student.idStudent)
                                putExtra("studentName", student.name)
                                putExtra("studentSurname", student.surname)
                                putExtra("subjectName", subjectName)
                                putExtra("subjectId", subjectId)
                            }
                            startActivity(intent)
                        }

                        view.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                            lifecycleScope.launch {
                                val db = AppDatabase.getInstance(this@SubjectDetailsActivity)
                                db.enrollmentDao().deleteStudentFromSubject(student.idStudent,subjectId)

                                runOnUiThread {
                                    Toast.makeText(
                                        this@SubjectDetailsActivity,
                                        "Student deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadSubjectStudents(subjectId, subjectName)
                                }
                            }
                        }
                        return view
                    }
                }
                findViewById<ListView>(R.id.list_view_items).adapter = adapter
            }
        }
    }
}