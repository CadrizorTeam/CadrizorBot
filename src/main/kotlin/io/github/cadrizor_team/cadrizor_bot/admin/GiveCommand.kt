package io.github.cadrizor_team.cadrizor_bot.admin

import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import io.github.cadrizor_team.cadrizor_bot.lang.splitOn
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendErrorEmbed
import io.github.cadrizor_team.cadrizor_bot.utils.getMember
import java.lang.Integer.parseInt
import kotlin.math.abs

object GiveCommand {
	@JvmStatic
	fun give() = literal<GMREvent>("give")
			.requires(GMREvent::isOwner)
			.then(argument<GMREvent, String>("other", string())
					.then(argument<GMREvent, String>("resources", greedyString())
							.executes {
								val event = it.source
								val guild = event.guild
								val member = it.getMember("other")
								if (member == null) {
									sendErrorEmbed(event, "Member not found")
									return@executes -1
								}
								val result = retrieveData(guild, member)
								if (result.isCreated) {
									sendErrorEmbed(event, "No save data for <@!${member.user.id}>")
									return@executes -1
								}

								val inventory = deserialize(result.memberTag)
								val giftMap = HashMap<String, Int>()
								val gives = getString(it, "resources").trim().splitOn(";")
								for (currentGive in gives) {
									val cgs = currentGive.splitOn(":").map(String::trim)
									val type = cgs[0]
									val amount = abs(parseInt(cgs[1]))
									if (amount != 0) {
										giftMap[type] = amount
										addResource(inventory, type, from(amount))
									}
								}
								inventory.storeData(member)

								val gift = CadrizEmbedBuilder.result("Admin / Give", member)
										.appendDescription("\nFrom <@!${event.member!!.user.id}> | ${event.member!!.user.id}")
								if ("wood" in giftMap || "cobble" in giftMap || "stone" in giftMap)
									gift.addField(
											"Basic Resources",
											"Wood ${from(giftMap["wood"] ?: 0).repr()}" +
													"\nCobble ${from(giftMap["cobble"] ?: 0).repr()}" +
													"\nStone ${from(giftMap["stone"] ?: 0).repr()}",
											true)
								if ("charcoal" in giftMap || "coal" in giftMap)
									gift.addField(
											"Fuels",
											"Charcoal ${from(giftMap["charcoal"] ?: 0).repr()}" +
													"\nCoal ${from(giftMap["coal"] ?: 0).repr()}",
											true
									)
								if ("iron_ore" in giftMap || "gold_ore" in giftMap
										|| "diamond_ore" in giftMap || "emerald_ore" in giftMap)
									gift.addField(
											"Ores",
											"Iron ${from(giftMap["iron_ore"] ?: 0).repr()}" +
													"\nGold ${from(giftMap["gold_ore"] ?: 0).repr()}" +
													"\nDiamond ${from(giftMap["diamond_ore"] ?: 0).repr()}" +
													"\nEmerald ${from(giftMap["emerald_ore"] ?: 0).repr()}",
											true
									)
								if ("iron" in giftMap || "gold" in giftMap || "diamond" in giftMap || "emerald" in giftMap)
									gift.addField(
											"Ingots | Gems",
											"Iron ${from(giftMap["iron"] ?: 0).repr()}" +
													"\nGold ${from(giftMap["gold"] ?: 0).repr()}" +
													"\nDiamond ${from(giftMap["diamond"] ?: 0).repr()}" +
													"\nEmerald ${from(giftMap["emerald"] ?: 0).repr()}",
											true
									)
								if ("iron_block" in giftMap || "gold_block" in giftMap
										|| "diamond_block" in giftMap || "emerald_block" in giftMap)
									gift.addField(
											"Blocks",
											"Iron ${from(giftMap["iron_block"] ?: 0).repr()}" +
													"\nGold ${from(giftMap["gold_block"] ?: 0).repr()}" +
													"\nDiamond ${from(giftMap["diamond_block"] ?: 0).repr()}" +
													"\nEmerald ${from(giftMap["emerald_block"] ?: 0).repr()}",
											true
									)
								if ("diamerald" in giftMap || "diamerald_block" in giftMap)
									gift.addField(
											"Alloys",
											"Diamerald ${from(giftMap["diamerald"] ?: 0).repr()}" +
													"\n**Blocks** ${from(giftMap["diamerald_block"] ?: 0).repr()}",
											true
									)
								if ("obsidian" in giftMap || "wither_skull" in giftMap || "nether_star" in giftMap)
									gift.addField(
											"Rare Resources",
											"Obsidian ${from(giftMap["obsidian"] ?: 0).repr()}" +
													"\nWither Skulls ${from(giftMap["wither_skull"] ?: 0).repr()}" +
													"\nNether Stars ${from(giftMap["nether_star"] ?: 0).repr()}",
											true
									)
								if ("singularity" in giftMap || "ultimate" in giftMap
										|| "cadrizor" in giftMap || "cadriz" in giftMap)
									gift.addField(
											"Extreme Resources",
											"Singularities ${from(giftMap["singularity"] ?: 0).repr()}" +
													"\nUltimate ${from(giftMap["ultimate"] ?: 0).repr()}" +
													"\nCadrizor ${from(giftMap["cadrizor"] ?: 0).repr()}" +
													"\nCadriz ${from(giftMap["cadriz"] ?: 0).repr()}",
											true
									)

								gift.send(event)
								1
							})
			)!!

	private fun addResource(inventory: MemberInventory, type: String, amount: NumberFactory) {
		when (type) {
			"wood" -> inventory.wood += amount.get()
			"charcoal" -> inventory.charcoal += amount.get()
			"coal" -> inventory.coal += amount.get()
			"cobble" -> inventory.cobble += amount.get()
			"stone" -> inventory.stone += amount.get()
			"iron_ore" -> inventory.ironOre += amount.get()
			"iron" -> inventory.ironIngot += amount.get()
			"iron_block" -> inventory.ironBlock += amount.get()
			"gold_ore" -> inventory.goldOre += amount.get()
			"gold" -> inventory.goldIngot += amount.get()
			"gold_block" -> inventory.goldBlock += amount.get()
			"diamond_ore" -> inventory.diamondOre += amount.get()
			"diamond" -> inventory.diamond += amount.get()
			"diamond_block" -> inventory.diamondBlock += amount.get()
			"emerald_ore" -> inventory.emeraldOre += amount.get()
			"emerald" -> inventory.emerald += amount.get()
			"emerald_block" -> inventory.emeraldBlock += amount.get()
			"diamerald" -> inventory.crushedDiamerald += amount.get()
			"diamerald_block" -> inventory.crushedDiameraldBlock += amount.get()
			"obsidian" -> inventory.obsidian += amount.get()
			"wither_skull" -> inventory.netherStar += amount.get()
			"nether_star" -> inventory.netherStar += amount.get()
			"singularity" -> inventory.singularity += amount.get()
			"ultimate" -> inventory.ultimate += amount.get()
			"cadrizor" -> inventory.cadrizOre += amount.get()
			"cadriz" -> inventory.cadrizIngot += amount.get()
		}
	}
}