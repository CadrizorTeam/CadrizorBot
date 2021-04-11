package io.github.cadrizor_team.cadrizor_bot

import io.github.cadrizor_team.cadrizor_bot.lang.multDivide

class NumberFactory
@JvmOverloads constructor(var M: Int = 0, var k: Int = 0, var u: Int = 0) {
	fun get() = M * 1000000 + k * 1000 + u

	private fun checkBounds() {
		if (k >= 1000) {
			k -= 1000
			M++
		}
		if (u >= 1000) {
			u -= 1000
			k++
		}
	}

	fun add(number: Int) {
		val other = from(number)
		u += other.u
		k += other.k
		M += other.M
		checkBounds()
	}

	fun repr(): String {
		if (M > 0) {
			if (M >= 100) return "$M.${k / 100}M"
			if (M >= 10) return if (k / 10 < 10) "$M.0${k / 10}M" else "$M.${k / 10}M"
			if (k < 10) return "$M.00${k}M"
			return if (k < 100) "$M.0${k}M" else "$M.${k}M"
		}
		if (k > 0) {
			if (k >= 100) return "$k.${u / 100}k"
			if (k >= 10) return if (u / 10 < 10) "$k.0${u / 10}k" else "$k.${u / 10}k"
			if (u < 10) return "$k.00${u}k"
			return if (u < 100) "$k.0${u}k" else "$k.${u}k"
		}
		return "$u"
	}

	override fun toString() = repr()

	companion object {
		@JvmStatic
		fun zero() = NumberFactory().get()

		@JvmStatic
		fun from(number: Int) = NumberFactory().apply {
			var base = number
			if (base >= 1000000) {
				M = base / 1000000
				base -= base multDivide 1000000
			}
			if (base >= 1000) {
				k = base / 1000
				base -= base multDivide 1000
			}
			u = base
		}
	}
}