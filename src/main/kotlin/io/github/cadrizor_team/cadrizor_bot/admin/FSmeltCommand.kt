package io.github.cadrizor_team.cadrizor_bot.admin

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import io.github.cadrizor_team.cadrizor_bot.utils.getMember

object FSmeltCommand {
	@JvmStatic
	fun fsmelt() = literal<GMREvent>("fsmelt")
			.then(smeltMe())
			.then(smeltOther())
			.requires(GMREvent::isOwner)!!

	private fun smeltMe() = literal<GMREvent>("me")
			.then(literal<GMREvent>("all")
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val inventory = MemberInventory.deserialize(result.memberTag)
						smeltAll(member, inventory)

						sendVolatileMessage(event, "Smelted all your inventory")
						1
					})
			.then(argument<GMREvent, String>("type", word())
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val inventory = MemberInventory.deserialize(result.memberTag)
						val type = getString(it, "type")
						smeltType(member, inventory, type)

						sendVolatileMessage(event, "Smelted all your $type !")
						1
					})!!

	private fun smeltOther() = literal<GMREvent>("other")
			.then(argument<GMREvent, String>("name", word())
					.then(literal<GMREvent>("all")
							.executes {
								val event = it.source
								it.getMember("name")?.let { member ->
									val result = retrieveData(event.guild, member)
									if (result.isCreated) {
										sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
										return@executes -1
									}

									val inventory = MemberInventory.deserialize(result.memberTag)
									smeltAll(member, inventory)

									sendVolatileMessage(event, "${event.member!!.effectiveName} smelted all your inventory !")
									return@executes 1
								}
								-1
							})
					.then(argument<GMREvent, String>("type", word())
							.executes {
								val event = it.source
								val type = getString(it, "type")
								it.getMember("name")?.let { member ->
									val result = retrieveData(event.guild, member)
									if (result.isCreated) {
										sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
										return@executes -1
									}

									val inventory = MemberInventory.deserialize(result.memberTag)
									smeltType(member, inventory, type)

									sendVolatileMessage(event, "${event.member!!.effectiveName} smelted all your $type !")
									return@executes 1
								}
								-1
							}))!!
}