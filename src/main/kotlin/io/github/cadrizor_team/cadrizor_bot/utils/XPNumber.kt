package io.github.cadrizor_team.cadrizor_bot.utils

import io.github.cadrizor_team.cadrizor_bot.NumberFactory

class XPNumber(private val xp: NumberFactory = NumberFactory(u = 0)) {
	val exp: Int get() = getLevelXp()
	val level: Int get() = getLevel()

	internal fun rawXP() = xp.get()

	fun addXp(number: Int): Boolean {
		val baseLevel = level
		xp.add(number)
		val endLevel = level
		return endLevel > baseLevel
	}

	fun display(): String {
		val max = XPLevels.XP_CAPS_FACTORIES[getLevel() + 1].get()
		return Utils.progress(getLevelXp(), max)
	}

	@JvmName("-getLevel")
	private fun getLevel(): Int {
		var exp = rawXP()
		var level = 0
		while (exp > XPLevels.XP_CAPS_FACTORIES[level + 1].get()) {
			exp -= XPLevels.XP_CAPS_FACTORIES[level + 1].get()
			level += 1
		}
		return level
	}

	private fun getLevelXp(): Int {
		var exp = rawXP()
		var level = 0
		while (exp > XPLevels.XP_CAPS_FACTORIES[level + 1].get()) {
			exp -= XPLevels.XP_CAPS_FACTORIES[level + 1].get()
			level += 1
		}
		return exp
	}
}