package com.example.lesson_8_fokin.presentation

import androidx.fragment.app.Fragment

interface NavigationController {
    fun navigate(fragment: Fragment)
    fun back()
}