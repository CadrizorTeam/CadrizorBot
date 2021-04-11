package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import io.github.cadrizor_team.cadrizor_bot.utils.getMember

object PayCommand {
	@JvmStatic
	fun pay() = literal<GMREvent>("pay")
			.then(other())
			.executes {
				CadrizEmbedBuilder.wrongUsage("pay \"<@member>\" <amount>")
						.send(it.source)
				1
			}!!

	private fun other() = argument<GMREvent, String>("other", string())
			.then(argument<GMREvent, Int>("amount", integer(1))
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val other = it.getMember("other")
						if (other == null) {
							CadrizEmbedBuilder.error("Member not found").send(event)
							return@executes -1
						}

						val inventory = MemberInventory.deserialize(result.memberTag)
						val money = getInteger(it, "amount")
						if (money > inventory.money) {
							CadrizEmbedBuilder.error("Not enough money")
									.addField("Missing", "${from((money - inventory.money).toInt()).repr()}¤", true)
									.send(event)
							return@executes -1
						}

						val otherResult = retrieveData(event.guild, other)
						val otherInventory = MemberInventory.deserialize(otherResult.memberTag)

						inventory.money -= money.toLong()
						inventory.storeData(member)
						otherInventory.money += money.toLong()
						otherInventory.storeData(other)

						CadrizEmbedBuilder.result("Money Sent", member)
								.addField("Receiver", "<@!${other.user.id}>", true)
								.addField("Amount", "${from(money)}¤", true)
								.send(event)
						1
					})!!
}