package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.CraftingManager
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object PicksCommand {
	@JvmStatic
	fun picks() = literal<GMREvent>("picks")
			.then(select())
			.then(setName())
			.then(repair())
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val minv = MemberInventory.deserialize(result.memberTag)
				val pickaxes = minv.pickaxes.filter { pick -> pick.miningLevel != HarvestLevel.HAND }

				val pickaxesEmbed = CadrizEmbedBuilder.result("Picks", member)
						.appendDescription("\nYou currently have ${pickaxes.size} / ${minv.pickaxeSlots} picks")
				for (i in pickaxes.indices) {
					val pickaxe = pickaxes[i]
					var content = "${pickaxe.miningLevel.name}\n"
					if (pickaxe.miningLevel != HarvestLevel.HAND)
						content += progress(pickaxe.durability, getDurability(pickaxe.miningLevel.durability, minv))
					if (minv.pickaxeIndex == i) content = ":white_check_mark: $content"
					pickaxesEmbed.addField(pickaxe.getName(), content, true)
				}
				pickaxesEmbed.send(event)
				1
			}!!

	private fun select() = literal<GMREvent>("select")
			.then(argument<GMREvent, Int>("index", integer(0))
					.executes {
						val event = it.source
						val member = event.member!!
						val result = DataStorage.retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						var index = getInteger(it, "index")
						val inventory = MemberInventory.deserialize(result.memberTag)
						if (index >= inventory.pickaxeSlots) index = inventory.pickaxeSlots - 1
						inventory.pickaxeIndex = index
						inventory.storeData(member)
						1
					})!!

	private fun setName() = literal<GMREvent>("setname")
			.then(argument<GMREvent, Int>("index", integer(0))
					.then(argument<GMREvent, String>("name", string())
							.executes {
								val event = it.source
								val member = event.member!!
								val result = DataStorage.retrieveData(event.guild, member)
								if (result.isCreated) {
									sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
									return@executes -1
								}

								val inventory = MemberInventory.deserialize(result.memberTag)
								var index = getInteger(it, "index")
								if (index >= inventory.pickaxeSlots) index = inventory.pickaxeSlots - 1
								val oldName = inventory.pickaxes[index].getName()
								val name = getString(it, "name")
								inventory.pickaxes[index].name = name
								inventory.storeData(member)
								sendVolatileMessage(event, "Renamed pickaxe ($index) from `$oldName` to `$name`")
								1
							}))!!

	private fun repair() = literal<GMREvent>("repair")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inventory = MemberInventory.deserialize(result.memberTag)
				if (!inventory.pickaxe.isBroken) {
					sendVolatileMessage(event, ":x: Your pickaxe is not broken.")
					return@executes -1
				}
				val recipe = CraftingManager["pick_${inventory.pickaxe.miningLevel.name.toLowerCase()}"]
				if (recipe == null) {
					CadrizEmbedBuilder.error("Cannot repair tool")
							.addField("Unknown recipe", "pick_${inventory.pickaxe.miningLevel.name.toLowerCase()}", true)
							.send(event)
					return@executes -1
				}
				if (CraftingManager.craftSpecial(member, recipe, inventory)) {
					CadrizEmbedBuilder.result("Pickaxe repaired", member)
							.addField("Harvest Level", inventory.pickaxe.miningLevel.name, true)
							.addField("Durability", progress(
									inventory.pickaxe.durability,
									getDurability(inventory.pickaxe.miningLevel.durability, inventory)
							), true)
							.send(event)
					return@executes 1
				}
				-1
			}!!
}