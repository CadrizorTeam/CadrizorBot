package io.github.cadrizor_team.cadrizor_bot.utils

import io.github.cadrizor_team.cadrizor_bot.Main
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity

object ActivityUtils {
	internal fun countGuilds() = Main.shardManager.shards.map { it.guilds.size }.reduce { prev, it -> prev + it }

	internal fun refreshPresence() {
		val guildsNumber = countGuilds()
		Main.shardManager.setPresence(if (Main.PROCESS_COMMANDS) OnlineStatus.ONLINE else OnlineStatus.DO_NOT_DISTURB,
				Activity.playing(if (Main.PROCESS_COMMANDS) "$guildsNumber guild${if (guildsNumber > 1) "s" else ""} | ${Main.VERSION}" else "Idle")
		)
	}
}