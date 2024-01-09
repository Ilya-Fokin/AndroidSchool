package com.example.lesson_12_fokin.presentation

import androidx.fragment.app.Fragment

interface NavigationController {
    fun navigate(fragment: Fragment)
    fun navigateWithoutBack(fragment: Fragment)
    fun back()
}