package com.example.teacherassistant2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var buttonSubjectList: Button
    private lateinit var buttonStudentList: Button
    private lateinit var fragmentContainer: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonStudentList=findViewById(R.id.studentList)
        buttonSubjectList=findViewById(R.id.subjectList)
        fragmentContainer=findViewById(R.id.fragment_container)

        buttonStudentList.setOnClickListener{
            startActivity(Intent(this, StudentListActivity::class.java))
        }

        buttonSubjectList.setOnClickListener{
            startActivity(Intent(this, SubjectListActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if(supportFragmentManager.backStackEntryCount == 1)
            {
                supportFragmentManager.popBackStack()
            }

            else
            {
                supportFragmentManager.popBackStack()
            }

        } else {
            super.onBackPressed()
        }
    }
}