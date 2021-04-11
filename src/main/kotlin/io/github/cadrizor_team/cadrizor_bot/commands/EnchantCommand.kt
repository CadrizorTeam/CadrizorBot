package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.setFrom
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object EnchantCommand {
	@JvmStatic
	fun enchant() = literal<GMREvent>("enchant")
			.then(enchantPart())
			.then(makeList())
			.executes {
				CadrizEmbedBuilder
						.wrongUsage("enchant list|(enchant [part [<enchantment>]])")
						.send(it.source)
				1
			}!!

	private fun enchantPart() = literal<GMREvent>("enchant")
			.then(argument<GMREvent, String>("part", word())
					.then(argument<GMREvent, String>("enchantment", word())
							.executes {
								val event = it.source
								val member = event.member!!
								val result = retrieveData(event.guild, member)
								if (result.isCreated) {
									sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
									return@executes -1
								}
								val inventory = MemberInventory.deserialize(result.memberTag)
								if (!inventory.enchantbench) {
									CadrizEmbedBuilder
											.missingFactory("Enchantbench", "${Main.prefixes.user}craft enchantbench")
											.send(event)
									return@executes -1
								}

								val part = getString(it, "part")
								val enchPart = Enchantment.EnchantablePart.byName(part)
								if (enchPart == Enchantment.EnchantablePart.UNKNOWN) {
									CadrizEmbedBuilder.error("Unknown EnchantablePart")
											.addField("Wrong Value", part, true)
											.send(event)
									return@executes -1
								}

								val enchantName = getString(it, "enchantment")
								val enchantment = EnchantmentsInit[enchantName]
								if (enchantment == null || !enchantment.canApply(enchPart)) {
									CadrizEmbedBuilder.error("Unknown or incompatible Enchant")
											.addField("Enchant", enchantName, true)
											.addField("Part", enchPart.name, true)
											.send(event)
									return@executes -1
								}

								val map = when (enchPart) {
									Enchantment.EnchantablePart.AXE -> HashMap(inventory.axeEnchants)
									Enchantment.EnchantablePart.PICKAXE -> HashMap(inventory.pickaxe.enchants)
									Enchantment.EnchantablePart.SWORD -> HashMap(inventory.swordEnchants)
									else -> return@executes -1
								}

								val baseLevel = map[enchantment] ?: 0
								if (baseLevel + 1 > enchantment.maxLevel) {
									CadrizEmbedBuilder.error("Enchant has reached the max level !")
											.addField("Enchant", enchantName, true)
											.addField("Level", "${from(enchantment.maxLevel)}", true)
											.addField("Part", enchPart.name, true)
											.send(event)
									return@executes -1
								}

								val recipe = enchantment.recipe(baseLevel)
								if (recipe == null || !recipe.craft(inventory)) {
									CadrizEmbedBuilder.error("Unable to Enchant")
											.addField("Part", enchPart.name, true)
											.addField("Enchant", enchantName, true)
											.addField("Level", "${from(baseLevel)}", true)
											.send(event)
									return@executes -1
								}

								map.replace(enchantment, baseLevel + 1)
								when (enchPart) {
									Enchantment.EnchantablePart.AXE -> inventory.axeEnchants.setFrom(map)
									Enchantment.EnchantablePart.PICKAXE -> inventory.pickaxe.enchants.setFrom(map)
									Enchantment.EnchantablePart.SWORD -> inventory.swordEnchants.setFrom(map)
									else -> return@executes -1
								}

								inventory.storeData(member)

								CadrizEmbedBuilder.result("Enchanting Table", member)
										.addField("Successfully Enchanted", enchPart.name, true)
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
						val inventory = MemberInventory.deserialize(result.memberTag)
						if (!inventory.enchantbench) {
							CadrizEmbedBuilder.missingFactory("Enchantbench", "${Main.prefixes.user}craft enchantbench").send(event)
							return@executes -1
						}

						val part = getString(it, "part")
						val enchPart = Enchantment.EnchantablePart.byName(part)
						if (enchPart === Enchantment.EnchantablePart.UNKNOWN) {
							CadrizEmbedBuilder.error("Unknown EnchantablePart").addField("Wrong Value", part, true).send(event)
							return@executes -1
						}

						val enchants = StringBuilder("Available enchantments for ${part.toLowerCase()}")
						loop@
						for (enchantment in EnchantmentsInit.ENCHANTMENTS) {
							if (enchantment.canApply(enchPart)) {
								val map = when (enchPart) {
									Enchantment.EnchantablePart.AXE -> HashMap(inventory.axeEnchants)
									Enchantment.EnchantablePart.PICKAXE -> HashMap(inventory.pickaxe.enchants)
									Enchantment.EnchantablePart.SWORD -> HashMap(inventory.swordEnchants)
									else -> continue@loop
								}

								val baseLevel = map.getOrDefault(enchantment, 0)
								if (baseLevel + 1 > enchantment.maxLevel) continue

								enchantment.recipe(baseLevel + 1)?.let { recipe ->
									enchants.append("\n`${enchantment.id}` - ${enchantment.name} : ${recipe.simpleCosts(inventory)}")
								}
							}
						}

						sendVolatileMessage(event, enchants.toString())
						1
					}
			)!!

	private fun makeList() = literal<GMREvent>("list")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}
				val inventory = MemberInventory.deserialize(result.memberTag)
				if (!inventory.enchantbench)
					CadrizEmbedBuilder
							.missingFactory("Enchantbench", "${Main.prefixes.user}craft enchantbench")
							.send(event)
				val message = StringBuilder("Enchantments List :")
				for (enchantment in EnchantmentsInit.ENCHANTMENTS)
					enchantment.apply {
						message.append("\n- $name (`$id` | $maxLevel level${if (maxLevel > 1) "s" else ""}) : ")
								.append(EnchantmentsInit.ENCHANTMENTS_DESCRIPTION[this])
					}
				sendVolatileMessage(event, message.toString())
				1
			}!!
}