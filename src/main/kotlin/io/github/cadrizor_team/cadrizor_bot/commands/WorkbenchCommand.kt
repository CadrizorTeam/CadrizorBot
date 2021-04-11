package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object WorkbenchCommand {
	@JvmStatic
	fun workbench() = literal<GMREvent>("workbench")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}
				val inventory = MemberInventory.deserialize(result.memberTag)
				if (inventory.workbench) {
					CadrizEmbedBuilder.error("You already have a workbench !")
							.addField("You should use", "${Main.prefixes.user}craft", true)
							.send(event)
					return@executes -1
				}
				if (inventory.wood < 50) {
					CadrizEmbedBuilder.invMissingItem("Wood", 50)
							.addField("Progress", progress(inventory.wood, 50), true)
							.send(event)
					return@executes -1
				}
				inventory.wood -= 50
				inventory.workbench = true
				inventory.storeData(member)

				CadrizEmbedBuilder.result("Workbench Bought", member)
						.addField("You can now use", "${Main.prefixes.user}craft [<recipe>]", true)
						.send(event)
				1
			}!!
}