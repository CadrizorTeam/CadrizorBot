package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.BonusesStorage
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.dataStorageFile
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils
import net.dv8tion.jda.api.entities.Message
import java.time.Duration

object DumpCommand {
	fun dump() = literal<GMREvent>("dump")
			.then(commands())
			.then(data())
			.executes {
				CadrizEmbedBuilder
						.wrongUsage("dump commands|(data bonuses|members)")
						.send(it.source)
				1
			}!!

	private fun commands() = literal<GMREvent>("commands")
			.executes {
				var length = 0
				val currentMsg = StringBuilder()
				val messagesToSend = ArrayList<String>()
				val messagePairs = Main.USER_DISPATCHER.getSmartUsage(Main.USER_DISPATCHER.root, it.source)
						.map { e -> "${Main.prefixes.user}${e.value}" }
						.map { s -> Pair(s.length, s) }
				messagePairs.forEach { p ->
					if (length + p.first + 1 < 2000) {
						currentMsg.append(p.second).append("\n")
						length += p.first
					} else {
						messagesToSend += "$currentMsg"
						currentMsg.clear().append(p.second)
						length = p.first
					}
				}
				currentMsg.takeIf(StringBuilder::isNotEmpty)?.let {
					messagesToSend += "$currentMsg"
					currentMsg.clear()
				}
				messagesToSend.forEach { msg -> Utils.sendVolatileMessage(it.source, msg) }
				1
			}!!

	private fun data() = literal<GMREvent>("data")
			.then(literal<GMREvent>("bonuses")
					.executes {
						it.source.channel.sendMessage(
								CadrizEmbedBuilder.result("Dumping Bonuses file", it.source.member!!).build()
						)
								.addFile(BonusesStorage.bonusFile.inputStream(), "bonus.dat")
								.delay(Duration.ofSeconds(150))
								.flatMap(Message::delete)
								.queue()
						1
					})
			.then(literal<GMREvent>("members")
					.executes {
						val event = it.source
						val guild = event.guild
						val file = guild.dataStorageFile
						val name = "data-g${guild.id}-${guild.name.replace(" +".toRegex(), "+")}.dat"
						val embed = CadrizEmbedBuilder.result("Dumping Save file", event.member!!)
								.addField("Guild ID", guild.id, true)
								.addField("Guild Name", guild.name, true)
						event.channel.sendMessage(embed.build())
								.addFile(file, name)
								.delay(Duration.ofMinutes(4))
								.flatMap(Message::delete)
								.queue()
						1
					})!!
}