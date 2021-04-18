package io.github.cadrizor_team.cadrizor_bot.commands

import com.flowpowered.nbt.CompoundMap
import com.flowpowered.nbt.CompoundTag
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object PrestigeCommand {
	private fun canPrestige(inventory: MemberInventory) = inventory.cadrizIngot >= inventory.prestige.minPrestige

	@JvmStatic
	fun prestige() = literal<GMREvent>("prestige")
			.then(confirm())
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inventory = MemberInventory.deserialize(result.memberTag)

				val prestige = CadrizEmbedBuilder.result("Prestige / Informations", member)
						.addField("Number of Prestige Resets", "${from(inventory.prestige.nbPrestige)}", true)
						.addField("Prestige Points", "${from(inventory.prestige.prestige)}", true)
						.addField("Next Prestige Progression", progress(inventory.cadrizIngot, inventory.prestige.minPrestige), false)
				if (canPrestige(inventory))
					prestige.addField("Prestige ?", "${Main.prefixes.user}prestige confirm", true)
				prestige.send(event)
				return@executes 1
			}!!

	private fun confirm() = literal<GMREvent>("confirm")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inventory = MemberInventory.deserialize(result.memberTag)

				if (canPrestige(inventory)) {
					inventory.prestige.prestige += inventory.cadrizIngot
					inventory.prestige.lastPrestige = inventory.cadrizIngot
					inventory.prestige.nbPrestige += 1

					val tag = CompoundTag("m${member.user.id}", CompoundMap())
					inventory.serialize(tag)
					tag.value["inventory"] = CompoundTag("inventory", CompoundMap())

					val inventory2 = MemberInventory.deserialize(tag)
					inventory2.applyStorageUpgrade()
					inventory2.storeData(member)

					CadrizEmbedBuilder.result("Prestige / Success", member)
							.addField("Prestige Resets", "${from(inventory.prestige.nbPrestige)}", true)
							.addField("Prestige Points", "${from(inventory.prestige.prestige)}", true)
							.send(event)
					return@executes 1
				}

				CadrizEmbedBuilder.invMissingItem("Cadriz", inventory.prestige.minPrestige)
						.addField("Progress", progress(inventory.cadrizIngot, inventory.prestige.minPrestige), true)
						.send(event)
				1
			}!!
}