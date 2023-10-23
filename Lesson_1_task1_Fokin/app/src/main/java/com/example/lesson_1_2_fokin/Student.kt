package com.example.lesson_1_2_fokin

import java.time.Year

data class Student (val id: Long = System.currentTimeMillis(), val firstName: String, val secondName: String,
                    var yearOfBirth: String = "", var grade: String = ""): Comparable<Student> {
    override fun compareTo(other: Student): Int {
        return if (secondName == other.secondName) {
            firstName.compareTo(other.firstName)
        } else secondName.compareTo(other.secondName)
    }

    override fun toString(): String {
        return "$id $firstName $secondName $yearOfBirth $grade"
    }

}