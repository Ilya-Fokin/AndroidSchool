package com.example.lesson_0_fokin

import com.example.lesson_0_fokin.Creature

interface Fight {
    fun doFight(creature: Creature): Creature
    fun doHealing(): String
    fun die(): String
}