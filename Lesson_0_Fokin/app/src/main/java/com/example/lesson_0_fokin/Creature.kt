package com.example.lesson_0_fokin

import kotlin.random.Random

abstract class Creature(
    private val attack: Int,
    private val protection: Int,
    private val maxHealth: Int,
    private val damage: Array<Int>): Fight {

    abstract var healing: Int
    abstract var state: State
    protected var currentHealth: Double = maxHealth.toDouble()

    override fun doFight(creature: Creature): Creature {
        val attackModifier: Int = attackModifier(creature)

        for (i in 0 until attackModifier) {
            val result = (Math.random() * 6 + 1).toInt()
            if (result == 5 || result == 6) {
                val damage = damage[Random.nextInt(this.damage.size)]
                creature.currentHealth = creature.currentHealth - damage
                break
            }
            Thread.sleep(1000)
        }
        if (creature.currentHealth < 1) {
            creature.die()
        }
        return creature
    }

    override fun doHealing(): String {
        var result: String = ""
        if (currentHealth == maxHealth.toDouble()) {
            result = "У Вас максимальное здоровье"
        }
        if (healing == 0) {
            result = "Запасы лексира исчерпаны"
        }
        if (currentHealth < maxHealth) {
            currentHealth += maxHealth * 0.3
            if (currentHealth >= maxHealth) {
                currentHealth = maxHealth.toDouble()
            }
            healing--
            result = "Ваше здоровье восполнено - $currentHealth"
        }
        return result
    }

    override fun die(): String {
        TODO("Not yet implemented")
    }

    private fun attackModifier(creature: Creature): Int {
        val attackModifier: Int = this.attack - creature.protection + 1
        return if (attackModifier < 0) {
            1
        } else attackModifier;
    }
}