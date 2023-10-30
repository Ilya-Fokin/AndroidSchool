package com.example.lesson_1_fokin

object Validator {
        fun checkFullName(fullName: String): Boolean {
            val chars: CharArray = fullName.toCharArray()
            if (fullName.split("\\s+".toRegex()).count() != 2) {
                return false
            }
            repeat(chars.size) {index ->
                if (chars[index].isDigit()) {
                    return false
                }
            }
            return true
        }

        fun checkFullNameAndGradeAndYear(str: String): Boolean {
            if (str.split("\\s+".toRegex()).count() != 4) {
                return false
            }
            val strArr: List<String> = str.split(" ")
            if (strArr[2].toCharArray().size !in 2..3) {
                return false
            }
            if (strArr[3].toCharArray().size != 4 && !strArr[3].all { char -> char.isDigit() }) {
                return false
            }
            return true
        }

}