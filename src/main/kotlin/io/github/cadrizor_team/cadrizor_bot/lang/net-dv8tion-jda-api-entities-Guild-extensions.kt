@file:JvmName("GuildExtensions")

package io.github.cadrizor_team.cadrizor_bot.lang

import io.github.cadrizor_team.cadrizor_bot.Main
import net.dv8tion.jda.api.entities.Guild

val Guild.commandsCadrizorChannel
	get() = getTextChannelsByName("commands-cadrizor${if (Main.dev) "-dev" else ""}", false)
			.run { if (size > 0) this[0] else null }