package com.example.lesson_1_2_fokin

import java.util.Collections

data class Journal<T: MutableCollection<Student>>(val journal: T): JournalInter<T> {
    override fun addStudent(student: Student): Boolean {
        return journal.add(student)
    }

    override fun getAllStudent(): List<Student> {
        return journal.sorted()
    }
}