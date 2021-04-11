package io.github.cadrizor_team.cadrizor_bot.utils

import net.dv8tion.jda.api.entities.Member

interface IEmbedable {
	fun getEmbed(member: Member): CadrizEmbedBuilder
}