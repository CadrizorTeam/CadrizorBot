package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.commands.SmeltingProcess.defineTimes
import io.github.cadrizor_team.cadrizor_bot.commands.SmeltingProcess.doSmelting
import io.github.cadrizor_team.cadrizor_bot.emotes.CadrizEmotes
import io.github.cadrizor_team.cadrizor_bot.emotes.DiscordEmotes
import io.github.cadrizor_team.cadrizor_bot.handling.reactions.CraftingReactionHandler
import io.github.cadrizor_team.cadrizor_bot.handling.reactions.SmeltReactionHandler
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.SmeltingLevel.NONE
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object SmeltCommand {
	@JvmStatic
	fun smelt() = literal<GMREvent>("smelt")
			.then(smeltSmelt())
			.then(smeltUpgrade())
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inv = MemberInventory.deserialize(result.memberTag)
				val level = inv.furnaceLevel
				if (level == NONE) {
					CadrizEmbedBuilder
							.missingFactory("Furnace", "${Main.prefixes.user}craft furnace")
							.send(event)
					return@executes -1
				}

				CadrizEmbedBuilder.result("Furnace Interface", member)
						.addField("Max Smelting", "${PrestigeUtils.getFurnaceSize(level, inv)}", true)
						.addField("Output Dupe", "x${level.smeltingResult}", true)
						.addField("Smelt", "c%smelt smelt", true)
						.addField("Upgrade", "c%smelt upgrade", true)
						.send(event)
				1
			}!!

	private fun smeltUpgrade() = literal<GMREvent>("upgrade")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inventory = MemberInventory.deserialize(result.memberTag)
				val level = inventory.furnaceLevel
				level.nextTierRecipe?.let { recipe ->
					val costs = recipe.costs(inventory)
					if (recipe.canCraft(inventory))
						event.channel.sendMessage("$costs\n${CadrizEmotes.Crafting} = Craft").complete()
					else {
						sendVolatileMessage(event, costs)
						null
					}
				}?.let { message ->
					CadrizEmotes.Crafting.send(message)
					message.addReaction(DiscordEmotes.NG).queue()
					Main.MESSAGES_HANDLERS[message.id] = CraftingReactionHandler(member, level.nextTierRecipe)
				}
				1
			}!!

	private fun smeltSmelt() = literal<GMREvent>("smelt")
			.then(argument<GMREvent, String>("id", word())
					.then(argument<GMREvent, Int>("times", integer(1))
							.executes {
								val event = it.source
								val member = event.member!!
								val result = DataStorage.retrieveData(event.guild, member)
								if (result.isCreated) {
									sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
									return@executes -1
								}

								val minv = MemberInventory.deserialize(result.memberTag)
								val level = minv.furnaceLevel
								if (level == NONE) {
									CadrizEmbedBuilder
											.missingFactory("Furnace", "${Main.prefixes.user}craft furnace")
											.send(event)
									return@executes -1
								}

								val choice = getString(it, "id")
								val nbTimes = defineTimes(minv, choice, getInteger(it, "times"), PrestigeUtils.getFurnaceSize(level, minv))
								val message = doSmelting(minv, choice, nbTimes)

								minv.storeData(member)
								sendVolatileMessage(event, message)
								1
							})
					.executes {
						val event = it.source
						val member = event.member!!
						val result = DataStorage.retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val inv = MemberInventory.deserialize(result.memberTag)
						val level = inv.furnaceLevel
						if (level === NONE) {
							CadrizEmbedBuilder
									.missingFactory("Furnace", "${Main.prefixes.user}craft furnace")
									.send(event)
							return@executes -1
						}

						CadrizEmbedBuilder
								.result("Smelting Interface", member)
								.addField("Resource", getString(it, "id"), true)
								.addField("Count", "0", true)
								.addField("Max", "${PrestigeUtils.getFurnaceSize(level, inv)}", true)
								.send(event) { message ->
									message.addReaction(DiscordEmotes.number0).queue()
									message.addReaction(DiscordEmotes.number1).queue()
									message.addReaction(DiscordEmotes.number2).queue()
									message.addReaction(DiscordEmotes.number3).queue()
									message.addReaction(DiscordEmotes.number4).queue()
									message.addReaction(DiscordEmotes.number5).queue()
									message.addReaction(DiscordEmotes.number6).queue()
									message.addReaction(DiscordEmotes.number7).queue()
									message.addReaction(DiscordEmotes.number8).queue()
									message.addReaction(DiscordEmotes.number9).queue()
									message.addReaction(DiscordEmotes.NG).queue()
									message.addReaction(DiscordEmotes.BACK).queue()
									message.addReaction(DiscordEmotes.OK).queue()

									Main.MESSAGES_HANDLERS[message.id] = SmeltReactionHandler(member, getString(it, "id"))
								}
						1
					})
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inv = MemberInventory.deserialize(result.memberTag)
				val level = inv.furnaceLevel
				if (level == NONE) {
					CadrizEmbedBuilder.missingFactory("Furnace", "${Main.prefixes.user}craft furnace").send(event)
					return@executes -1
				}

				val e: (Int) -> String = { am -> if (am > 0) ":white_check_mark:" else ":x:" }
				var message = "${e(inv.wood)} 1 Wood -> ${level.smeltingResult} Charcoal"
				message += "\n${e(inv.cobble)} 1 Cobble -> ${level.smeltingResult} Stone"
				message += "\n${e(inv.ironOre)} 1 Iron Ore -> ${level.smeltingResult} Iron Ingot"
				message += "\n${e(inv.goldOre)} 1 Gold Ore -> ${level.smeltingResult} Gold Ingot"
				message += "\n${e(inv.diamondOre)} 1 Diamond Ore -> ${level.smeltingResult} Diamond"
				message += "\n${e(inv.emeraldOre)} 1 Emerald Ore -> ${level.smeltingResult} Emerald"
				message += "\n${e(inv.cadrizOre)} 1 Cadrizor -> ${level.smeltingResult} Cadriz"
				message += "\n`smelt smelt wood|cobble|iron_ore|gold_ore|diamond_ore|emerald_ore|cadriz_ore [<times>]`"
				sendVolatileMessage(event, message)
				1
			}!!
}