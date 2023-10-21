package com.example.lesson_1_2_fokin

import java.time.Year

data class Student (val id: Long = System.currentTimeMillis(), val firstName: String, val secondName: String): Comparable<Student> {
    lateinit var yearOfBirth: String
    lateinit var grade: String
    override fun compareTo(other: Student): Int {
        return if (secondName == other.secondName) {
            firstName.compareTo(other.firstName)
        } else secondName.compareTo(other.secondName)
    }

    override fun toString(): String {
        return "Student (id=$id, firstName='$firstName', secondName='$secondName',yearOfBirth='$yearOfBirth', grade='$grade')"
    }

}