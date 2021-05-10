package io.github.cadrizor_team.cadrizor_bot.handling.reactions

import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.commands.SmeltingProcess
import io.github.cadrizor_team.cadrizor_bot.emotes.DiscordEmotes
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils
import io.github.cadrizor_team.cadrizor_bot.utils.Utils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction

class SmeltReactionHandler(member: Member, private val choice: String) : MessageReactionHandler(member) {
	override fun handle(message: Message, inventory: MemberInventory, emote: MessageReaction.ReactionEmote, name: String): Boolean {
		message.removeReaction(name, requiredMember.user).queue()
		return when {
			name == DiscordEmotes.NG -> {
				message.delete().queue()
				true
			}
			name == DiscordEmotes.OK -> {
				val embed = message.embeds[0]
				val field = embed.fields.firstOrNull { it.name == "Count" }
				val value = try {
					Integer.parseInt(field?.value ?: "0")
				} catch (exception: Exception) {
					0
				}
				val times = SmeltingProcess.defineTimes(inventory, choice, value, PrestigeUtils.getFurnaceSize(inventory.furnaceLevel, inventory))
				Utils.sendVolatileMessage(message.textChannel, SmeltingProcess.doSmelting(inventory, choice, times))
				Main.MESSAGES_HANDLERS -= message.id
				message.delete().queue()
				true
			}
			message.embeds.size > 0 && message.embeds[0].fields.size > 0 -> {
				val embed = message.embeds[0]
				val field = embed.fields.firstOrNull { it.name == "Count" }
				val value = (field?.value ?: "0").let {
					if (name == DiscordEmotes.BACK)
						try {
							val number = Integer.parseInt(it)
							if (number < 10) "0" else it.substring(0 until it.length - 1)
						} catch (exception: NumberFormatException) {
							"0"
						}
					else "${
						try {
							Integer.parseInt(DiscordEmotes.addNumberTo(it, name))
						} catch (exception: NumberFormatException) {
							it
						}
					}"
				}
				val builder = EmbedBuilder(embed).clearFields()
				embed.fields.forEach {
					if (it.name != "Count") builder.addField(it)
					else builder.addField(field?.name, value, field?.isInline ?: true)
				}
				message.editMessage(builder.build()).queue()
				false
			}
			else -> false
		}
	}
}