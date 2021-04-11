package io.github.cadrizor_team.cadrizor_bot.lang

import io.github.cadrizor_team.cadrizor_bot.NumberFactory

class BooleanHashMap
@JvmOverloads constructor(boolean: Boolean, val map: HashMap<String, NumberFactory> = HashMap()) {
	var boolean: Boolean = boolean
		private set

	fun setTrue(): BooleanHashMap {
		boolean = true
		return this
	}

	fun setFalse(): BooleanHashMap {
		boolean = false
		return this
	}
}