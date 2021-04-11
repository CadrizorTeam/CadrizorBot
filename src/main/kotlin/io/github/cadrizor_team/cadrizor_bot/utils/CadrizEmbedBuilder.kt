package io.github.cadrizor_team.cadrizor_bot.utils

import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.lang.commandsCadrizorChannel
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.awt.Color
import java.time.Duration
import java.time.temporal.TemporalAccessor

class CadrizEmbedBuilder : EmbedBuilder {
	private constructor()

	private constructor(builder: EmbedBuilder) : super(builder)

	override fun addField(name: String?, value: String?, inline: Boolean) = super.addField(name, value, inline) as CadrizEmbedBuilder

	override fun appendDescription(description: CharSequence) = super.appendDescription(description) as CadrizEmbedBuilder

	override fun setColor(color: Color?) = super.setColor(color) as CadrizEmbedBuilder

	override fun setColor(color: Int) = super.setColor(color) as CadrizEmbedBuilder

	override fun setTitle(title: String?) = super.setTitle(title) as CadrizEmbedBuilder

	override fun setFooter(text: String?) = super.setFooter(text) as CadrizEmbedBuilder

	override fun setTimestamp(temporal: TemporalAccessor?) = super.setTimestamp(temporal) as CadrizEmbedBuilder

	fun sendPrivate(user: User) = user.openPrivateChannel()
			.flatMap { it.sendMessage(build()) }.flatMap { it.privateChannel.close() }.queue()

	fun send(event: GuildMessageReceivedEvent, consumer: ((Message) -> Unit)? = null) {
		val action = event.channel.sendMessage(build())
		if (consumer != null) action.queue(consumer)
		else action.delay(Duration.ofSeconds(90))
				.flatMap<Void> { it.delete() }
				.queue()
	}

	fun send(guild: Guild) {
		guild.commandsCadrizorChannel?.let { sendVolatileEmbed(it, this) }
	}

	companion object {
		fun from(builder: EmbedBuilder) = CadrizEmbedBuilder(builder)

		fun create() = CadrizEmbedBuilder()

		fun result(title: String, member: Member) = create()
				.setTitle("Cadrizor Bot - $title")
				.setColor(member.color?.rgb ?: 0x00FF00)
				.appendDescription("Member: <@!${member.user.id}> | ID: ${member.user.id}")

		fun error(description: String) = create()
				.setTitle("Cadrizor Bot - Error")
				.setColor(0xFF0000)
				.appendDescription(description)

		fun wrongUsage(command: String) = create()
				.setTitle("Cadrizor Bot - Wrong Command Usage")
				.setColor(0x000000)
				.addField("Usage", "`${Main.prefixes.user}${command}`", true)

		@JvmOverloads
		fun invMissingItem(type: String, amount: Int = 1): CadrizEmbedBuilder {
			val embed = error("Item is missing in your inventory").addField("Missing", type, true)
			if (amount > 1) embed.addField("Amount", "${NumberFactory.from(amount)}", true)
			return embed
		}

		fun toolBroken(tool: String) = error("Your tool is broken").addField("Tool", tool, true)

		fun missingFactory(missing: String, command: String): CadrizEmbedBuilder {
			val embed = error("Missing Factory")
			embed.addField("Factory", missing, true)
			if (command.isNotEmpty()) embed.addField("To Do", command, true)
			return embed
		}
	}
}