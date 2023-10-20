package com.example.lesson_1_2_fokin

class Validator {
    companion object{
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
    }
}