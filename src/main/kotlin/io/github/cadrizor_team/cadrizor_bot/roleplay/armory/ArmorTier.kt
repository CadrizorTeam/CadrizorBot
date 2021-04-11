package io.github.cadrizor_team.cadrizor_bot.roleplay.armory

enum class ArmorTier(val protection: Int, val maxDamage: Int) {
	NONE(0, 0),
	WOODEN(1, 100),
	STONE(2, 250);

	operator fun next() = when (this) {
		NONE -> WOODEN
		WOODEN -> STONE
		else -> null
	}

	operator fun hasNext() = this != STONE
}