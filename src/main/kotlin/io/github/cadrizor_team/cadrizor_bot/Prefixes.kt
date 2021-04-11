package io.github.cadrizor_team.cadrizor_bot

interface IPrefixes {
	val user: String
	val admin: String
}

class NormalPrefixes : IPrefixes {
	override val user get() = "c%"
	override val admin get() = "ca%"
}

class DevelopmentPrefixes : IPrefixes {
	override val user get() = "d%"
	override val admin get() = "da%"
}