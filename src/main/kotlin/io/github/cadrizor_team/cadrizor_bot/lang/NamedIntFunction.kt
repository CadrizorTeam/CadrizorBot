package io.github.cadrizor_team.cadrizor_bot.lang

class NamedIntFunction(val name: String, private val function: (Int) -> Int) : (Int) -> Int {
	override fun invoke(integer: Int) = function(integer)
}