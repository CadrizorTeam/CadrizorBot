package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.armory.ArmorType
import io.github.cadrizor_team.cadrizor_bot.roleplay.armory.ArmorType.Companion.fromID
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder.Companion.missingFactory
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

object ArmorCraftCommand {
	@JvmStatic
	fun armorCraft() = literal<GMREvent>("armorcraft")
			.then(argument<GMREvent, String>("type", word())
					.then(literal<GMREvent>("upgrade")
							.executes {
								val event = it.source
								val member = event.member
								val result = retrieveData(event.guild, member!!)
								if (result.isCreated) {
									sendVolatileMessage(event, ":warning: You don't have IG account. We created one fot you. Please retry after 15s.")
									return@executes -1
								}
								val inventory = deserialize(result.memberTag)
								if (!inventory.armorbench) {
									missingFactory("Armorbench", "${Main.prefixes.user}craft armorbench").send(event)
									return@executes -1
								}
								val armors = inventory.armor
								val message = when (fromID(getString(it, "type"))) {
									ArmorType.HELMET -> getUpgradeMessage(inventory, ArmorType.HELMET, armors.helmet.craftingRecipe)
									ArmorType.CHESTPLATE -> getUpgradeMessage(inventory, ArmorType.CHESTPLATE, armors.chestplate.craftingRecipe)
									ArmorType.LEGGINGS -> getUpgradeMessage(inventory, ArmorType.LEGGINGS, armors.leggings.craftingRecipe)
									ArmorType.BOOTS -> getUpgradeMessage(inventory, ArmorType.BOOTS, armors.boots.craftingRecipe)
									else -> getUpgradeMessage(inventory, ArmorType.UNKNOWN, null)
								}
								inventory.storeData(member)
								sendVolatileMessage(event, message)
								1
							})
					.executes {
						val event = it.source
						val member = event.member
						val result = retrieveData(event.guild, member!!)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one fot you. Please retry after 15s.")
							return@executes -1
						}
						val inventory = deserialize(result.memberTag)
						if (!inventory.armorbench) {
							missingFactory("Armorbench", "${Main.prefixes.user}craft armorbench").send(event)
							return@executes -1
						}
						val armors = inventory.armor
						val type = fromID(getString(it, "type"))
						val canCraft = when (type) {
							ArmorType.HELMET -> if (armors.helmet.next() != null) armors.helmet.craftingRecipe!!.canCraft(inventory) else false
							ArmorType.CHESTPLATE -> if (armors.chestplate.next() != null) armors.chestplate.craftingRecipe!!.canCraft(inventory) else false
							ArmorType.LEGGINGS -> if (armors.leggings.next() != null) armors.leggings.craftingRecipe!!.canCraft(inventory) else false
							ArmorType.BOOTS -> if (armors.boots.next() != null) armors.boots.craftingRecipe!!.canCraft(inventory) else false
							else -> false
						}
						sendMessage(event, inventory, type, canCraft)
						1
					})
			.executes {
				val event = it.source
				val member = event.member
				val result = retrieveData(event.guild, member!!)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one fot you. Please retry after 15s.")
					return@executes -1
				}
				val inventory = deserialize(result.memberTag)
				if (!inventory.armorbench) {
					missingFactory("Armorbench", "${Main.prefixes.user}craft armorbench").send(event)
					return@executes -1
				}

				val armors = inventory.armor
				val helmet = if (armors.helmet.next() != null) armors.helmet.craftingRecipe!!.costs(inventory) else "Helmet :white_check_mark: MAXED"
				val chestplate = if (armors.chestplate.next() != null) armors.chestplate.craftingRecipe!!.costs(inventory) else "Chestplate :white_check_mark: MAXED"
				val leggings = if (armors.leggings.next() != null) armors.leggings.craftingRecipe!!.costs(inventory) else "Leggings :white_check_mark: MAXED"
				val boots = if (armors.boots.next() != null) armors.boots.craftingRecipe!!.costs(inventory) else "Boots :white_check_mark: MAXED"

				val message = "$helmet\n$chestplate\n$leggings\n$boots\n\nYou can use `${Main.prefixes.user}armorcraft [<part> [upgrade]]`"
				sendVolatileMessage(event, message)
				1
			}!!

	private fun sendMessage(event: GuildMessageReceivedEvent, inventory: MemberInventory, type: ArmorType, canCraft: Boolean) {
		var message = when (type) {
			ArmorType.HELMET -> "Helmet: ${getMessage(inventory, type)}"
			ArmorType.CHESTPLATE -> "Chestplate: ${getMessage(inventory, type)}"
			ArmorType.LEGGINGS -> "Leggings: ${getMessage(inventory, type)}"
			ArmorType.BOOTS -> "Boots: ${getMessage(inventory, type)}"
			ArmorType.UNKNOWN -> getMessage(inventory, type)
		}
		if (canCraft) message += "\nRun `${Main.prefixes.user}armorcraft ${type.id} upgrade` to upgrade your ${type.id} to the next tier"
		sendVolatileMessage(event, message)
	}

	private fun getUpgradeMessage(inventory: MemberInventory, part: ArmorType, recipe: Recipe?) = when (part) {
		ArmorType.HELMET -> {
			if (recipe!!.canCraft(inventory)) {
				recipe.craft(inventory)
				inventory.armor.helmet = inventory.armor.helmet.next()!!
				"Successfully upgraded your ${part.id} !"
			}
			"Cannot upgrade your ${part.id} !"
		}
		ArmorType.CHESTPLATE -> {
			if (recipe!!.canCraft(inventory)) {
				recipe.craft(inventory)
				inventory.armor.chestplate = inventory.armor.chestplate.next()!!
				"Successfully upgraded your ${part.id} !"
			}
			"Cannot upgrade your ${part.id} !"
		}
		ArmorType.LEGGINGS -> {
			if (recipe!!.canCraft(inventory)) {
				recipe.craft(inventory)
				inventory.armor.leggings = inventory.armor.leggings.next()!!
				"Successfully upgraded your ${part.id} !"
			}
			"Cannot upgrade your ${part.id} !"
		}
		ArmorType.BOOTS -> {
			if (recipe!!.canCraft(inventory)) {
				recipe.craft(inventory)
				inventory.armor.boots = inventory.armor.boots.next()!!
				"Successfully upgraded your ${part.id} !"
			}
			"Cannot upgrade your ${part.id} !"
		}
		else -> "Illegal armor part: ${part.id}"
	}

	private fun getMessage(inventory: MemberInventory, type: ArmorType) = when (type) {
		ArmorType.HELMET -> if (inventory.armor.helmet.next() != null) inventory.armor.helmet.craftingRecipe!!.costs(inventory) else ""
		ArmorType.CHESTPLATE -> if (inventory.armor.chestplate.next() != null) inventory.armor.chestplate.craftingRecipe!!.costs(inventory) else ""
		ArmorType.LEGGINGS -> if (inventory.armor.leggings.next() != null) inventory.armor.leggings.craftingRecipe!!.costs(inventory) else ""
		ArmorType.BOOTS -> if (inventory.armor.boots.next() != null) inventory.armor.boots.craftingRecipe!!.costs(inventory) else ""
		else -> "Illegal armor type: ${type.id}"
	}
}