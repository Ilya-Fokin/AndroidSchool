package com.example.lesson_0_fokin

object Validator {
    fun checkAttack(attack: Int): Boolean {
        return attack in 1..30
    }

    fun checkProtection(protection: Int): Boolean {
        return protection in 1..30
    }

    fun checkHealth(health: Int): Boolean {
        return health > 0
    }

    fun checkDamage(damage: IntArray): Boolean {
        repeat(damage.size - 1) { index ->
            if (damage[index] < 1) {
                return false
            }
        }
        return true
    }
}