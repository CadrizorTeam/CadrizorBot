package io.github.cadrizor_team.cadrizor_bot.handling.reactions

import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction

abstract class MessageReactionHandler(val requiredMember: Member) {
	/**
	 * Should we remove the handler when [handle] is invoked ?
	 * @return `true` if [handle] erases the message, or if the handling should be done once.
	 */
	open fun removeHandler() = false

	/**
	 * @return Does [handle] edit [member][requiredMember]'s [inventory]
	 */
	abstract fun handle(message: Message, inventory: MemberInventory, emote: MessageReaction.ReactionEmote, name: String): Boolean
}