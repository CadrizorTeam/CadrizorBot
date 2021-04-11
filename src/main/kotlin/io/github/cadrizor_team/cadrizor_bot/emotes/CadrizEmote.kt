package io.github.cadrizor_team.cadrizor_bot.emotes

import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Message

class CadrizEmote(private val emote: Emote) {
	val name: String get() = emote.name

	fun send(message: Message) = message.addReaction(emote).queue()

	override fun equals(other: Any?) = other is CadrizEmote && other.emote.idLong == emote.idLong

	override fun toString() = emote.asMention

	override fun hashCode() = emote.hashCode()
}