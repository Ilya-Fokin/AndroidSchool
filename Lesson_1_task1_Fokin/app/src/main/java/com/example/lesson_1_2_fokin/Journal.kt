package com.example.lesson_1_2_fokin

interface Journal<T: MutableCollection<Student>> {
    fun addStudent(student: Student): Boolean
    fun getAllStudent(): List<Student>
}