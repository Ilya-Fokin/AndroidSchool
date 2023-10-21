package com.example.lesson_1_2_fokin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val journal: Journal<MutableList<Student>> = Journal(journal = mutableListOf())
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
                textViewWithAllStudents.append(journal.getAllStudent()[index].secondName + " "
                        + journal.getAllStudent()[index].firstName + "\n")
            }
        }

        findViewById<Button>(R.id.buttonModMainActivity).setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivityMod::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonSecondActivity).setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}

