package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object MoneyCommand {
	@JvmStatic
	fun money() = literal<GMREvent>("money")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inventory = MemberInventory.deserialize(result.memberTag)
				CadrizEmbedBuilder.result("Balance", member)
						.addField("Money", "${from(inventory.money.toInt())}¤", false)
						.send(event)
				1
			}!!
}