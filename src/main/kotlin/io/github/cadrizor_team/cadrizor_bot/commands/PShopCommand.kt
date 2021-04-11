package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.prestige.PrestigeShop
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object PShopCommand {
	@JvmStatic
	fun pshop() = literal<GMREvent>("pshop")
			.then(upgrade())
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val inv = MemberInventory.deserialize(result.memberTag)
				val prestige = CadrizEmbedBuilder.result("Prestige Upgrades / Informations", member)
						.appendDescription("\nPrestige Points: ${from(inv.prestige.prestige)}")

				for (upgrade in PrestigeShop.prestige.items) {
					val level = when (upgrade.id) {
						"storage" -> inv.prestige.lvlStorage
						"durability" -> inv.prestige.lvlDurability
						"furnace_size" -> inv.prestige.lvlFurnaceSize
						else -> throw IllegalStateException("Unknown upgrade ${upgrade.id}")
					}

					if (level != -1)
						prestige.addField(
								upgrade.name,
								"`${upgrade.id}\n`" +
										"[$level] ${from(upgrade.upgrade(level))}% -> " +
										"[${level + 1}] ${from(upgrade.upgrade(level + 1))}%\n" +
										"**Cost:** ${from(upgrade.costs(level))}",
								true
						)
				}
				prestige.send(event)
				1
			}!!

	private fun upgrade() = argument<GMREvent, String>("upgrade", word())
			.then(literal<GMREvent>("buy")
					.executes {
						val event = it.source
						val member = event.member!!
						val result = retrieveData(event.guild, member)
						if (result.isCreated) {
							sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
							return@executes -1
						}

						val upgName = getString(it, "upgrade").toLowerCase()
						val upgrade = PrestigeShop.prestige[upgName]
						if (upgrade == null) {
							CadrizEmbedBuilder.error("Unknown prestige upgrade")
									.setTitle("PrestigeShop Error")
									.addField("You should use", "${Main.prefixes.user}prestige upgrades", true)
									.send(event)
							return@executes -1
						}

						val inv = MemberInventory.deserialize(result.memberTag)
						if (inv.prestige.nbPrestige == 0) {
							CadrizEmbedBuilder.error("You need to prestige to buy upgrades\nin the PrestigeShop")
									.setTitle("PrestigeShop Error")
									.send(event)
							return@executes 0
						}

						val level = when (upgName) {
							"storage" -> inv.prestige.lvlStorage
							"durability" -> inv.prestige.lvlDurability
							"furnace_size" -> inv.prestige.lvlFurnaceSize
							else -> throw IllegalStateException("Unknown upgrade $upgName")
						}

						if (level == -1) {
							CadrizEmbedBuilder.error("An unknown error occurred")
									.setTitle("PrestigeShop Error")
									.send(event)
							return@executes -1
						}

						if (inv.prestige.prestige >= upgrade.costs(level)) {
							if (upgrade.buyUpgrade(inv, level)) {
								inv.storeData(member)
								sendVolatileMessage(event, ":white_check_mark: Upgrade `$upgName` bought !")
								return@executes 1
							}
							CadrizEmbedBuilder.error("Cannot buy upgrade")
									.setTitle("PrestigeShop Error")
									.addField("Upgrade", upgName, true)
									.send(event)
							return@executes 1
						}
						if (inv.prestige.lastPrestige != 0)
							CadrizEmbedBuilder.invMissingItem("Prestige Points", upgrade.costs(level))
									.addField("Upgrade", upgName, true)
									.send(event)
						else
							CadrizEmbedBuilder.error("Prestige is needed to buy Upgrades !")
									.setTitle("PrestigeShop Error")
									.send(event)
						1
					})
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}

				val upgName = getString(it, "upgrade").toLowerCase()
				val upgrade = PrestigeShop.prestige[upgName]
				if (upgrade == null) {
					CadrizEmbedBuilder.error("Unknown prestige upgrade")
							.setTitle("PrestigeShop Error")
							.addField("You should use", "${Main.prefixes.user}prestige upgrades", true)
							.send(event)
					return@executes -1
				}

				val inv = MemberInventory.deserialize(result.memberTag)
				if (inv.prestige.nbPrestige == 0) {
					CadrizEmbedBuilder.error("You need to prestige to buy upgrades\nin the PrestigeShop")
							.setTitle("PrestigeShop Error")
							.send(event)
					return@executes 0
				}

				val level = when (upgName) {
					"storage" -> inv.prestige.lvlStorage
					"durability" -> inv.prestige.lvlDurability
					"furnace_size" -> inv.prestige.lvlFurnaceSize
					else -> -1
				}
				if (level == -1) {
					CadrizEmbedBuilder.error("An unknown error occurred")
							.setTitle("PrestigeShop Error")
							.send(event)
					return@executes -1
				}

				CadrizEmbedBuilder.result("Prestige Upgrades / Informations", member)
						.appendDescription("\nPrestige Points: ${from(inv.prestige.prestige)}")
						.addField("Current Level", "[$level] ${from(upgrade.upgrade(level))}%", true)
						.addField("Next Level", "[${level + 1}] ${from(upgrade.upgrade(level + 1))}%", true)
						.addField("Upgrade Cost", "${from(upgrade.costs(level))}", true)
						.send(event)
				1
			}!!
}