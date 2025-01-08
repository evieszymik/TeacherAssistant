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
        }

        loadStudents()
    }

    private fun loadStudents() {
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
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = convertView ?: layoutInflater.inflate(R.layout.student_item, parent, false)
                        val student = getItem(position)!!

                        view.findViewById<TextView>(R.id.text_view_item).text =
                            "${student.name} ${student.surname}"

                        // Obsługa kliknięcia na uczestnika
                        /*view.setOnClickListener {
                            val intent = Intent(this@StudentListActivity, ParticipantDetailsActivity::class.java).apply {
                                putExtra("participantId", participant.idUczestnika)
                                putExtra("participantName", participant.imie)
                                putExtra("participantSurname", participant.nazwisko)
                            }
                            startActivity(intent)
                        }*/

                        // Obsługa przycisków edycji i usuwania
                        /*view.findViewById<Button>(R.id.btn_edit).setOnClickListener {
                            val editIntent = Intent(this@ShowParticipantsActivity, AddParticipantActivity::class.java).apply {
                                putExtra("participantId", participant.idUczestnika)
                                putExtra("participantName", participant.imie)
                                putExtra("participantSurname", participant.nazwisko)
                            }
                            startActivity(editIntent)
                        }*/

                        /*view.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                            lifecycleScope.launch {
                                val db = AppDatabase.getInstance(this@ShowParticipantsActivity)
                                // Najpierw usuń powiązania z wydarzeniami
                                db.wydarzenieUczestnikDao().deleteByUczestnikId(participant.idUczestnika)
                                // Następnie usuń uczestnika
                                db.uczestnikDao().delete(participant)

                                runOnUiThread {
                                    Toast.makeText(
                                        this@ShowParticipantsActivity,
                                        "Uczestnik usunięty",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadParticipants() // Odśwież listę
                                }
                            }
                        }*/

                        return view
                    }
                }
                findViewById<ListView>(R.id.list_view_items).adapter = adapter
            }
        }
    }
}