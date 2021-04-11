package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.mobs.Mobs.MOBS
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object MobsCommand {
	@JvmStatic
	fun mobs() = literal<GMREvent>("mobs")
			.then(argument<GMREvent, String>("ID", word())
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val inventory = deserialize(result.memberTag)
						if (inventory.swordDurability < -1) {
							CadrizEmbedBuilder.invMissingItem("Sword").send(event)
							return@executes 0
						}
						if (inventory.swordDurability == 0) {
							CadrizEmbedBuilder.toolBroken("Sword")
									.appendDescription("\nPlease craft another one")
									.send(event)
							return@executes 0
						}

						val mob = MOBS[getString(it, "ID")]
						if (mob == null) {
							CadrizEmbedBuilder.error("Unknown Mob ID").send(event)
							return@executes -1
						} else if (mob.id == "wither" && inventory.witherSkulls >= inventory.maxWitherSkulls - 1) {
							CadrizEmbedBuilder.error("Storage is Full")
									.addField("Resource", "Wither Skulls", true)
									.send(event)
							return@executes 0
						}
						mob.fight(event, member, inventory)
						1
					})
			.executes {
				val event = it.source
				val message = StringBuilder("Mobs List:")
				for ((id, mob) in MOBS)
					message.append("\n`$id`: ${mob.name}")
				sendVolatileMessage(event, "$message")
				1
			}!!
}