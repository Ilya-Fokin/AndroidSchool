package com.example.lesson_1_fokin

interface Journal<T: MutableCollection<Student>> {
    fun addStudent(student: Student): Boolean
    fun getAllStudent(): List<Student>
}
