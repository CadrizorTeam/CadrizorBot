package io.github.cadrizor_team.cadrizor_bot.handling.events

import io.github.cadrizor_team.cadrizor_bot.CadrizChecker
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.emotes.CadrizEmotes
import io.github.cadrizor_team.cadrizor_bot.utils.ActivityUtils
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import net.dv8tion.jda.api.events.ReadyEvent

object Ready : EventHandler<ReadyEvent> {
	override fun listen(event: ReadyEvent) {
		println("Welcome from Cadrizor@[${Main.VERSION}]")
		Main.TC_Cadrizor_admins = Main.shardManager.getTextChannelById(614817898824859673L)!!
		CadrizEmotes.init(Main.TC_Cadrizor_admins.guild)
		if (!Main.dev) CadrizChecker.checkPerms()
		ActivityUtils.refreshPresence()
		Main.GUILDS_LISTENER.start()

		val embed = CadrizEmbedBuilder.create()
				.setTitle("Cadrizor Bot")
				.appendDescription((if (Main.dev) "Development " else "") + "Version ${Main.VERSION}")
				.addField("Links", "[『 Help 』](${Main.PROJECT_URL})\n[『 Issues 』](${Main.PROJECT_URL}-issues/)", true)
				.addField("Guilds", "${ActivityUtils.countGuilds()} guild(s)", true)
		Main.shardManager.guilds.forEach(embed::send)
	}
}