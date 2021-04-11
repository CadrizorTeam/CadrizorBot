package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object HarvestInfoCommand {
	@JvmStatic
	fun harvestInfo() = literal<GMREvent>("hvInf")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated)
					sendVolatileMessage(event, "You don't have IG account. We created one for you.")

				val inv = MemberInventory.deserialize(result.memberTag)

				val message = StringBuilder(":information_source: Harvest levels :")
				for (level in HarvestLevel.values()) {
					message.append("\n${level.name} [*$level*] Tools durability: ${if (level.durability < 0) "Infinite" else level.durability.toString()}")
					if (inv.prestige.nbPrestige > 0 && level.durability > 0)
						message.append(" - With Prestige: ").append(getDurability(level.durability, inv))
				}
				sendVolatileMessage(event, message.toString())
				1
			}!!
}