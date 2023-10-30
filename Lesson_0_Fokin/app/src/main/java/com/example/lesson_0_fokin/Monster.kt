package com.example.lesson_0_fokin

data class Monster(private val attack: Int,
                   private val protection: Int,
                   private val maxHealth: Int,
                   private val damage: Array<Int>): Creature(attack, protection, maxHealth, damage) {

    override fun die(): String {
        this.state = State.DIE
        return "Монстр мертв"
    }
}