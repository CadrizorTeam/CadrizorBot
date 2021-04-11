package io.github.cadrizor_team.cadrizor_bot.utils

import io.github.cadrizor_team.cadrizor_bot.NumberFactory

object XPLevels {
	internal val XP_CAPS_FACTORIES: Array<NumberFactory> by lazy {
		Array(400) { NumberFactory.from(7 * it * it + 70 * it + 150) }
	}
}