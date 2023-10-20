package com.example.lesson_0_fokin

data class Monster(private val attack: Int,
              private val protection: Int,
              private val maxHealth: Int,
              private val damage: Array<Int>
              ): Creature(attack, protection, maxHealth, damage) {
    override var healing: Int = 4
    override var state: State = State.ALIVE

    override fun die(): String {
        this.state = State.DIE
        return "Монстр мертв"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Monster

        if (attack != other.attack) return false
        if (protection != other.protection) return false
        if (maxHealth != other.maxHealth) return false
        if (currentHealth != other.currentHealth) return false
        if (!damage.contentEquals(other.damage)) return false
        if (healing != other.healing) return false
        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attack
        result = 31 * result + protection
        result = 31 * result + maxHealth
        result = 31 * result + currentHealth.hashCode()
        result = 31 * result + damage.contentHashCode()
        result = 31 * result + healing
        result = 31 * result + state.hashCode()
        return result
    }

    override fun toString(): String {
        return "Monster{" +
                "атака=" + attack +
                ", защита=" + protection +
                ", здоровье=" + currentHealth +
                ", исцеления=" + healing +
                ", урон=" + damage.contentToString() +
                ", состояние=" + state.toString() +
                '}';
    }
}