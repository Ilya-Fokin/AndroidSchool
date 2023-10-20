package com.example.lesson_1_2_fokin

import java.time.Year

data class Student (val firstName: String, val secondName: String): Comparable<Student> {
    lateinit var yearOfBirth: Year
    lateinit var schoolClass: String
    override fun compareTo(other: Student): Int {
        return if (secondName == other.secondName) {
            firstName.compareTo(other.firstName)
        } else secondName.compareTo(other.secondName)
    }
}