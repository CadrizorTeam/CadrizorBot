package io.github.cadrizor_team.cadrizor_bot.handling.reactions

import io.github.cadrizor_team.cadrizor_bot.emotes.CadrizEmote
import io.github.cadrizor_team.cadrizor_bot.emotes.CadrizEmotes
import io.github.cadrizor_team.cadrizor_bot.emotes.DiscordEmotes
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.CraftingManager
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction

class CraftingReactionHandler(member: Member, private val recipe: Recipe) : MessageReactionHandler(member) {
	constructor(member: Member, recipe: String): this(member, CraftingManager[recipe]!!)

	/**
	 * Should we remove the handler when [handle] is invoked ?
	 * @return `true` if [handle] erases the message, or if the handling should be done once.
	 */
	override fun removeHandler() = true

	/**
	 * @return Does [handle] edit [member][requiredMember]'s [inventory]
	 */
	override fun handle(message: Message, inventory: MemberInventory, emote: MessageReaction.ReactionEmote, name: String): Boolean {
		if (name == DiscordEmotes.NG) message.delete().queue()
		else if (CadrizEmote(emote.emote) == CadrizEmotes.Crafting) {
			message.delete().queue()
			CraftingManager.craftSpecial(requiredMember, recipe, inventory)
			CadrizEmbedBuilder.result("Item Crafted", requiredMember)
					.addField("Crafting", recipe.name, true)
					.send(requiredMember.guild)
			return true
		}
		return false
	}
}