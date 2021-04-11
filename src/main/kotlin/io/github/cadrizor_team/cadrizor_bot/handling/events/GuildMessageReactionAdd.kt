package io.github.cadrizor_team.cadrizor_bot.handling.events

import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent

object GuildMessageReactionAdd : EventHandler<GuildMessageReactionAddEvent> {
	override fun listen(event: GuildMessageReactionAddEvent) {
		if (!event.member.user.isBot) {
			val messageId = event.messageId
			if (messageId in Main.MESSAGES_HANDLERS) {
				val handler = Main.MESSAGES_HANDLERS[messageId]!!
				if (handler.requiredMember.id == event.member.id) {
					val self = event.guild.selfMember
					val channel = event.channel
					if (!self.hasPermission(channel, Permission.MESSAGE_HISTORY)) return
					channel.retrieveMessageById(messageId).queue {
						if (it.author.isBot && it.author.id == event.jda.selfUser.id) {
							val data = DataStorage.retrieveData(event.guild, event.member)
							val inventory = MemberInventory.deserialize(data.memberTag)

							if (handler.removeHandler()) Main.MESSAGES_HANDLERS -= messageId
							if (handler.handle(it, inventory, event.reactionEmote, event.reactionEmote.name))
								inventory.storeData(event.member)
						}
					}
				}
			}
		}
	}
}