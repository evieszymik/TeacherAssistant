package com.example.teacherassistant2

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class StudentListActivity : AppCompatActivity() {
    private lateinit var buttonAddStudent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonAddStudent=findViewById(R.id.buttonAddStudent)

        buttonAddStudent.setOnClickListener{
            startActivity(Intent(this, AddStudentActivity::class.java))
            finish()
        }

        loadStudents()
    }
    fun loadStudents() {
        lifecycleScope.launch {
            val students = AppDatabase.getInstance(this@StudentListActivity)
                .studentDao()
                .getAll()

            runOnUiThread {
                val adapter = object : ArrayAdapter<Student>(
                    this@StudentListActivity,
                    R.layout.student_item,
                    students
                ) {
                    @SuppressLint("SetTextI18n")
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = convertView ?: layoutInflater.inflate(R.layout.student_item, parent, false)
                        val student = getItem(position)!!

                        view.findViewById<TextView>(R.id.text_view_item).text =
                            "${student.name} ${student.surname}"


                        view.setOnClickListener {
                            val intent = Intent(this@StudentListActivity, StudentDetails2Activity::class.java).apply {
                                putExtra("studentId", student.idStudent)
                                putExtra("studentName", student.name)
                                putExtra("studentSurname", student.surname)
                                putExtra("studentAlbum", student.album)
                            }
                            startActivity(intent)
                        }

                        view.findViewById<Button>(R.id.btn_edit).setOnClickListener {
                            val editIntent = Intent(this@StudentListActivity, AddStudentActivity::class.java).apply {
                                putExtra("studentId", student.idStudent)
                                putExtra("studentName", student.name)
                                putExtra("studentSurname", student.surname)
                                putExtra("studentAlbum", student.album)
                            }
                            finish()
                            startActivity(editIntent)
                        }

                        view.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                            lifecycleScope.launch {
                                val db = AppDatabase.getInstance(this@StudentListActivity)
                                db.enrollmentDao().deleteByStudentId(student.idStudent)
                                db.studentDao().delete(student)

                                runOnUiThread {
                                    Toast.makeText(
                                        this@StudentListActivity,
                                        "Student deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadStudents()
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