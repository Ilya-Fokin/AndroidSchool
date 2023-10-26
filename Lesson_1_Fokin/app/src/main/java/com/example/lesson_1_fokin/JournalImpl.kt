package com.example.lesson_1_fokin

data class JournalImpl<T: MutableCollection<Student>>(val journal: T): Journal<T> {
    override fun addStudent(student: Student): Boolean {
        return journal.add(student)
    }

    override fun getAllStudent(): List<Student> {
        return journal.sorted()
    }
}