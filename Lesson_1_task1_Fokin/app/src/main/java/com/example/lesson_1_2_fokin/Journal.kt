package com.example.lesson_1_2_fokin

import java.util.Collections

data class Journal<T: MutableCollection<Student>>(val journal: T) {
    fun addStudent(student: Student): Boolean {
        return journal.add(student)
    }
    fun getAllStudent(): List<Student> {
        return journal.sorted()
    }
}