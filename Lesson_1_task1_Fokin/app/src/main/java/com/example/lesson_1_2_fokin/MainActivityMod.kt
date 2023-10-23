package com.example.lesson_1_2_fokin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.TreeSet

class MainActivityMod : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.mod_activity_main)
        val journalTreeSet = TreeSet<Student>()
    val journal: JournalImpl<TreeSet<Student>> = JournalImpl(journal = journalTreeSet)
    findViewById<Button>(R.id.buttonAddStudent).setOnClickListener {
        val input = findViewById<EditText>(R.id.editTextStudentData).text.toString()
        if (Validator.checkFullName(input)) {
            val (secondName, firstName) = input.split(" ")
            journal.addStudent(Student(firstName = firstName, secondName = secondName))
        } else Toast.makeText(this, "Неверный формат данных", Toast.LENGTH_SHORT).show()
    }

        findViewById<Button>(R.id.buttonShowAllStudent).setOnClickListener {
            val textViewWithAllStudents = findViewById<TextView>(R.id.textViewAllStudent)
            textViewWithAllStudents.text = ""
            repeat(journal.getAllStudent().size) {index ->
                textViewWithAllStudents.append("${journal.getAllStudent()[index].toString()}\n")
            }
        }

        findViewById<Button>(R.id.buttonMainActivity).setOnClickListener {
            super.finish()
        }
    }
}