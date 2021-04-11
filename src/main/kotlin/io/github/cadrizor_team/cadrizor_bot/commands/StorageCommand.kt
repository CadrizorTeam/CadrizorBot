package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object StorageCommand {
	@JvmStatic
	fun storage() = literal<GMREvent>("storage")
			.then(argument<GMREvent, String>("resource", word())
					.then(literal<GMREvent>("confirm")
							.executes {
								val event = it.source
								val member = event.member!!
								val result = retrieveData(event.guild, member)
								if (result.isCreated) {
									sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
									return@executes -1
								}
								val inventory = deserialize(result.memberTag)
								storageProcessConfirm(event, inventory, getString(it, "resource"))
								1
							})
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}
						val inventory = deserialize(result.memberTag)
						storageProcessResource(event, inventory, getString(it, "resource"))
						inventory.storeData(member)
						1
					})!!
}