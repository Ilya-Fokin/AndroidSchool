package com.example.lesson_1_2_fokin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.TreeSet

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val journalMap = HashMap<Long, Student>()

        val editText = findViewById<EditText>(R.id.editTextStudentData)
        editText.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val data = editText.text.toString()
                if (!Validator.checkFullNameAndGradeAndYear(data)) {
                    Toast.makeText(this, "Неверный формат данных", Toast.LENGTH_SHORT).show()
                } else {
                    val formatter = DateTimeFormatter.ofPattern("yyyy")
                    val dataArr = data.split(" ")
                    val student: Student = Student(firstName = dataArr[1], secondName = dataArr[0])
                    student.grade = dataArr[2]
                    student.yearOfBirth = dataArr[3]
                    journalMap[student.id] = student
                }
                return@setOnKeyListener true
            }
            false
        }

        findViewById<Button>(R.id.buttonShowAllStudent).setOnClickListener {
            val textViewWithAllStudents = findViewById<TextView>(R.id.textViewAllStudent)
            textViewWithAllStudents.text = ""
            for (elem in journalMap) {
                val student: Student = elem.value
                textViewWithAllStudents.append("${student.toString()} \n")
            }
        }

        findViewById<Button>(R.id.buttonMainActivity).setOnClickListener {
            super.finish()
        }
    }
}