package io.github.cadrizor_team.cadrizor_bot.handling.events

interface EventHandler<T> {
	fun listen(event: T)
}