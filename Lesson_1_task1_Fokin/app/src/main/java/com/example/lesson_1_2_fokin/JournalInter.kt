package com.example.lesson_1_2_fokin

interface JournalInter<T: MutableCollection<Student>> {
    fun addStudent(student: Student): Boolean
    fun getAllStudent(): List<Student>
}