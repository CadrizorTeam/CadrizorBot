package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.splitOnLineBreaks
import io.github.cadrizor_team.cadrizor_bot.roleplay.SwordLevel
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object SwordCommand {
	@JvmStatic
	fun sword() = literal<GMREvent>("sword")
			.then(literal<GMREvent>("upgrade")
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val inventory = deserialize(result.memberTag)
						val level = inventory.swordLevel

						if (level.hasNext() && level.nextRecipe != null) {
							val upgrade = level.nextRecipe
							if (upgrade.craft(inventory)) CadrizEmbedBuilder.result("Sword / Upgraded", member).send(event)
							else {
								val costs = upgrade.costs(inventory).splitOnLineBreaks()
								val newCosts = costs.copyOfRange(1, costs.size)
								CadrizEmbedBuilder.result("Sword / Cannot craft", member)
										.addField("Crafting Costs", newCosts.joinToString("\n"), true)
										.setColor(0xff8000)
										.send(event)
							}
							inventory.storeData(member)
							return@executes 1
						}

						CadrizEmbedBuilder.error("Your sword has reached the maximum level !").send(event)
						0
					})
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inv = deserialize(result.memberTag)
				val level = inv.swordLevel
				val durability = inv.swordDurability

				val swordBuilder = CadrizEmbedBuilder.result("Sword", member)
				if (level == SwordLevel.HAND)
					swordBuilder.addField("Sword", "*No Sword*", true)
				else {
					swordBuilder.addField("Sword Level", "$level", true)
					when {
						durability < 0 -> swordBuilder.addField("Sword Durability", "`Unknown`", true)
						durability == 0 -> swordBuilder.addField("Sword Durability", "Broken", true)
						else -> swordBuilder.addField("Sword Durability", progress(durability, getDurability(level.durability, inv)), true)
					}
					swordBuilder.addField("Sword Damage", "${level.damage}", true)
				}

				if (level.hasNext() && level.nextRecipe != null) {
					val costs = level.nextRecipe.costs(inv).splitOnLineBreaks()
					val newCosts = costs.copyOfRange(1, costs.size)
					swordBuilder.addField("Upgrade ?", newCosts.joinToString("\n"), false)
				}

				swordBuilder.send(event)
				1
			}!!
}