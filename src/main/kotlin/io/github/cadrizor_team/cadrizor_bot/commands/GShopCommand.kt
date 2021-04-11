package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global.GlobalShop
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object GShopCommand {
	@JvmStatic
	fun gshop(): LiteralArgumentBuilder<GMREvent> {
		val gshopCategoryItem = argument<GMREvent, String>("item", word())
		val gshopCategoryItemAction = argument<GMREvent, String>("action", word())

		gshopCategoryItemAction
				.executes {
					val event = it.source
					val member = event.member!!
					val category = getString(it, "category")
					val item = getString(it, "item")
					val gsc = GlobalShop[category]
					if (gsc == null) {
						CadrizEmbedBuilder.error("Category not found").addField("Category", category, true).send(event)
						return@executes -1
					}
					val gsi = gsc[item]
					if (gsi == null) {
						CadrizEmbedBuilder.error("Item not found").addField("Item", item, true).send(event)
						return@executes -1
					}

					val result = DataStorage.retrieveData(event.guild, member)
					if (result.isCreated) {
						sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
						return@executes -1
					}
					val inventory = MemberInventory.deserialize(result.memberTag)

					when (val action = getString(it, "action")) {
						"buy" -> {
							if (gsi.buyString == null) {
								CadrizEmbedBuilder.error("Cannot buy item").addField("Item", item, true).send(event)
								return@executes -1
							}

							if (!gsi.canBuy(inventory)) {
								CadrizEmbedBuilder.error("Cannot buy item").addField("Item", item, true).send(event)
								return@executes -1
							}
							gsi.onBuy(inventory)
						}
						"sell" -> {
							if (gsi.sellString == null) {
								CadrizEmbedBuilder.error("Cannot sell item").addField("Item", item, true).send(event)
								return@executes -1
							}

							if (!gsi.canSell(inventory)) {
								CadrizEmbedBuilder.error("Cannot sell item").addField("Item", item, true).send(event)
								return@executes -1
							}
							gsi.onSell(inventory)
							CadrizEmbedBuilder.result("Item sold", member)
									.addField("Item", item, true)
									.addField("Price", "+ ${gsi.sellString}", true)
									.send(event)
						}
						else -> {
							CadrizEmbedBuilder.error("Unknown Action")
									.addField("Action", action, true)
									.addField("Available", "buy/sell", true)
									.send(event)
							return@executes -1
						}
					}
					inventory.storeData(member)
					1
				}

		gshopCategoryItem
				.then(gshopCategoryItemAction)
				.executes {
					val event = it.source
					val member = event.member!!
					val category = getString(it, "category")
					val item = getString(it, "item")
					val gsc = GlobalShop[category]
					if (gsc == null) {
						CadrizEmbedBuilder.error("Category not found").addField("Category", category, true).send(event)
						return@executes -1
					}
					val gsi = gsc[item]
					if (gsi == null) {
						CadrizEmbedBuilder.error("Item not found").addField("Item", item, true).send(event)
						return@executes -1
					}

					val result = DataStorage.retrieveData(event.guild, member)
					if (result.isCreated) {
						sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
						return@executes -1
					}

					val inventory = MemberInventory.deserialize(result.memberTag)
					gsi.getEmbed(category, member, inventory).send(event)
					1
				}

		return literal<GMREvent>("gshop")
				.then(argument<GMREvent, String>("category", word())
						.then(gshopCategoryItem)
						.executes {
							val event = it.source
							val member = event.member!!
							val category = getString(it, "category")
							val shopCategory = GlobalShop[category] ?: return@executes -1
							shopCategory.getEmbed(member).send(event)
							1
						})
				.executes {
					GlobalShop.getEmbed(it.source.member!!).send(it.source)
					1
				}
	}
}