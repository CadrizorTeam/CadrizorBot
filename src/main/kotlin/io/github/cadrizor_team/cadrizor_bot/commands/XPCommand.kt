package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils

object XPCommand {
	@JvmStatic
	fun xp() = literal<GMREvent>("xp")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					Utils.sendVolatileMessage(event, "You don't have IG account. We created one for you.")
					return@executes -1
				}
				val inv = MemberInventory.deserialize(result.memberTag)
				CadrizEmbedBuilder.result("Experience", member)
						.addField("Level", "Current: ${inv.xp.level}\nClaimed: ${inv.claimedLvl}", true)
						.addField("Experience", inv.xp.display(), true)
						.send(event)
				1
			}!!
}