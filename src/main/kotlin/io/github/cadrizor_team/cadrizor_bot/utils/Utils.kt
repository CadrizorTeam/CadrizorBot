package io.github.cadrizor_team.cadrizor_bot.utils

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.text.DecimalFormat
import java.time.Duration

object Utils {
	private val format = DecimalFormat("##.###")

	@JvmStatic
	fun progress(cur: Int, max: Int) = "`${from(cur)}/${from(max)}` [**${format.format((cur.toFloat() / max.toFloat() * 100).toDouble())}%**]"

	@JvmStatic
	fun sendVolatileMessage(event: GuildMessageReceivedEvent, message: String) = sendVolatileMessage(event.channel, message)

	@JvmStatic
	fun sendVolatileMessage(channel: TextChannel, message: String) = channel.sendMessage(message)
			.delay(Duration.ofSeconds(90))
			.flatMap<Void> { it.delete() }
			.queue()

	@JvmStatic
	fun sendVolatileEmbed(channel: TextChannel, embed: EmbedBuilder) = channel.sendMessage(embed.build())
			.delay(Duration.ofSeconds(90))
			.flatMap<Void> { it.delete() }
			.queue()

	@JvmStatic
	fun sendErrorEmbed(event: GuildMessageReceivedEvent, error: String) = CadrizEmbedBuilder.error(error).send(event)

	@JvmStatic
	fun waitXSeconds(x: Int) = try {
		Thread.sleep((x * 1000).toLong())
	} catch (e: InterruptedException) {
		e.printStackTrace()
	}
}