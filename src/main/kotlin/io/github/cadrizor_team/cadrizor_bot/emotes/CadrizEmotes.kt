package io.github.cadrizor_team.cadrizor_bot.emotes

import net.dv8tion.jda.api.entities.Guild

object CadrizEmotes {
	lateinit var Crafting: CadrizEmote

	fun init(official: Guild) {
		Crafting = CadrizEmote(official.retrieveEmoteById(740187948561203251L).complete())
	}
}