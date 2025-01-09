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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SubjectListActivity : AppCompatActivity() {
    private lateinit var buttonAddSubject: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subject_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonAddSubject=findViewById(R.id.buttonAdd)
        buttonAddSubject.setOnClickListener{
            startActivity(Intent(this, AddSubjectActivity::class.java))
            finish()
        }

        loadSubjects()
    }
    private fun loadSubjects() {
        lifecycleScope.launch {
            val subjects = AppDatabase.getInstance(this@SubjectListActivity)
                .subjectDao()
                .getAll()


            runOnUiThread {
                val adapter = object : ArrayAdapter<Subject>(
                    this@SubjectListActivity,
                    R.layout.subject_item,
                    subjects
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = convertView ?: layoutInflater.inflate(
                            R.layout.subject_item,
                            parent,
                            false
                        )
                        val subject = getItem(position)!!

                        view.findViewById<TextView>(R.id.text_view_item).text = subject.name

                        view.setOnClickListener {
                            val intent = Intent(this@SubjectListActivity, SubjectDetailsActivity::class.java).apply {
                                putExtra("subjectId", subject.idSubject)
                                putExtra("subjectName", subject.name)
                                putExtra("subjectWeekday", subject.weekday)
                                putExtra("subjectHour", subject.time)
                            }
                            startActivity(intent)
                        }

                        view.findViewById<Button>(R.id.btn_edit).setOnClickListener {
                            val editIntent = Intent(this@SubjectListActivity, AddSubjectActivity::class.java).apply {
                                putExtra("subjectId", subject.idSubject)
                                putExtra("subjectName", subject.name)
                                putExtra("subjectWeekday", subject.weekday)
                                putExtra("subjectHour", subject.time)
                            }
                            finish()
                            startActivity(editIntent)
                        }

                        view.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                            lifecycleScope.launch {
                                val db = AppDatabase.getInstance(this@SubjectListActivity)
                                db.enrollmentDao().deleteBySubjectId(subject.idSubject)
                                db.subjectDao().delete(subject)

                                runOnUiThread {
                                    Toast.makeText(
                                        this@SubjectListActivity,
                                        "Subject deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadSubjects()
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