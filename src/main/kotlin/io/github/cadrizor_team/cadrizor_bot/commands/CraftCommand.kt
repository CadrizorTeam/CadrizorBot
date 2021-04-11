package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.emotes.CadrizEmotes
import io.github.cadrizor_team.cadrizor_bot.emotes.DiscordEmotes
import io.github.cadrizor_team.cadrizor_bot.handling.reactions.CraftingReactionHandler
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.CraftingManager
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import java.util.Comparator

object CraftCommand {
	@JvmStatic
	fun craft() = literal<GMREvent>("craft")
			.then(argument<GMREvent, String>("item", word())
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, "You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}
						val memberCompoundTag = result.memberTag
						val inventory = MemberInventory.deserialize(memberCompoundTag)
						if (!inventory.workbench) {
							CadrizEmbedBuilder
									.missingFactory("Workbench", "${Main.prefixes.user}workbench")
									.send(event)
							return@executes -1
						}

						CraftingManager.costs(event, inventory, getString(it, "item"))?.let { message ->
							CadrizEmotes.Crafting.send(message)
							message.addReaction(DiscordEmotes.NG).queue()
							Main.MESSAGES_HANDLERS[message.id] =
									CraftingReactionHandler(member, CraftingManager[getString(it, "item")]!!)
						}
						1
					}
			)
			.executes {
				val crafts = StringBuilder("Crafts **(you need a `workbench`)** :\n")
				CraftingManager.crafts.keys.stream()
						.sorted(Comparator.naturalOrder<String>()).forEach { s -> crafts.append("`$s`; ") }
				val msg = "${crafts.trim()}"
				sendVolatileMessage(it.source, msg.substring(0, msg.length - 1))
				1
			}!!
}