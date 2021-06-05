package io.github.cadrizor_team.cadrizor_bot.handling.events

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.utils.Utils

object GuildMessageReceived : EventHandler<GMREvent> {
	override fun listen(event: GMREvent) {
		val content = event.message.contentRaw
		if (!Main.PROCESS_COMMANDS || event.author.isBot || event.isWebhookMessage) return
		if ("${event.guild.id}|${event.author.id}" in Main.IGNORED) return

		if (content.startsWith(Main.prefixes.admin)) {
			if (event.channel.name != "commands-cadrizor${if (Main.dev) "-dev" else ""}") {
				Utils.sendVolatileMessage(event, ":x: You're not in a commands channel (`commands-cadrizor${if (Main.dev) "-dev" else ""}`)")
				return
			}
			event.message.delete().queue()
			val command = StringReader(content.substring(Main.prefixes.admin.length))
			try {
				Main.ADMIN_DISPATCHER.execute(command, event)
			} catch (e: CommandSyntaxException) {
				println(e.message)
			}
			return
		}
		if (content.startsWith(Main.prefixes.user)) {
			if (event.channel.name != "commands-cadrizor${if (Main.dev) "-dev" else ""}") {
				Utils.sendVolatileMessage(event, ":x: You're not in a commands channel (`commands-cadrizor${if (Main.dev) "-dev" else ""}`)")
				return
			}
			event.message.delete().queue()
			val command = StringReader(content.substring(Main.prefixes.user.length))
			try {
				Main.USER_DISPATCHER.execute(command, event)
			} catch (e: CommandSyntaxException) {
				println(e.message)
			}
			return
		}

		if (!content.startsWith(Main.prefixes.admin) && !content.startsWith(Main.prefixes.user)) {
			if (event.channel.name != "commands-cadrizor${if (Main.dev) "-dev" else ""}") return
			val message = event.message
			val name = event.member!!.effectiveName
			val id = event.author.id
			val textChannel = if (message.guild.idLong == event.guild.idLong)
				"<#${message.textChannel.id}>" else "*${message.textChannel.name}*"
			var sendMessage = "@$name (`$id`) said :\n> ${message.contentDisplay}"
			sendMessage += "\nin the channel $textChannel guild *`${message.guild.name}`*"
			sendMessage += "\nYou can use `ca%reset member this|all \"<@!$id>\"` or `ca%ignore add \"<@!$id>\"`"
			sendMessage += "\n*(NB: this only works in commands channels and for the Cadrizor @Owners)*"
			Main.TC_Cadrizor_admins.sendMessage(sendMessage).queue()
			message.delete().queue()
		}
	}
}