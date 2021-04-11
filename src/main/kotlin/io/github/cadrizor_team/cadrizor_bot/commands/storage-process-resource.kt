@file:JvmName("Storage_ProcessResource")
package io.github.cadrizor_team.cadrizor_bot.commands

import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

fun storageProcessResource(event: GuildMessageReceivedEvent, inventory: MemberInventory, name: String) {
	when (name) {
		"wood" -> processCheckMessage(event, "Wood", inventory.wood, inventory.maxWood)
		"charcoal" -> processCheckMessage(event, "Charcoal", inventory.charcoal, inventory.maxCharcoal)
		"coal" -> processCheckMessage(event, "Coal", inventory.coal, inventory.maxCoal)
		"cobble" -> processCheckMessage(event, "Cobble", inventory.cobble, inventory.maxCobble)
		"stone" -> processCheckMessage(event, "Stone", inventory.stone, inventory.maxStone)
		"iron_ore" -> processCheckMessage(event, "Iron Ore", inventory.ironOre, inventory.maxIronOre)
		"iron" -> processCheckMessage(event, "Iron", inventory.ironIngot, inventory.maxIronIngot)
		"iron_block" -> processCheckMessage(event, "Iron Block", inventory.ironBlock, inventory.maxIronBlock)
		"gold_ore" -> processCheckMessage(event, "Gold", inventory.goldOre, inventory.maxGoldOre)
		"gold" -> processCheckMessage(event, "Gold", inventory.goldIngot, inventory.maxGoldIngot)
		"gold_block" -> processCheckMessage(event, "Gold Block", inventory.goldBlock, inventory.maxGoldBlock)
		"diamond" -> processCheckMessage(event, "Diamond", inventory.diamond, inventory.maxDiamond)
		"diamond_block" -> processCheckMessage(event, "Diamond Block", inventory.diamondBlock, inventory.maxDiamondBlock)
		"emerald" -> processCheckMessage(event, "Emerald", inventory.emerald, inventory.maxEmerald)
		"emerald_block" -> processCheckMessage(event, "Emerald Block", inventory.emeraldBlock, inventory.maxEmeraldBlock)
		"diamerald" -> processCheckMessage(event, "Diamerald", inventory.crushedDiamerald, inventory.maxCrushedDiamerald)
		"diamerald_block" -> processCheckMessage(event, "Diamerald Block", inventory.crushedDiameraldBlock, inventory.maxCrushedDiameraldBlock)
		"obsidian" -> processCheckMessage(event, "Obsidian", inventory.obsidian, inventory.maxObsidian)
		"wither_skull" -> processCheckMessage(event, "Wither Skulls", inventory.witherSkulls, inventory.maxWitherSkulls)
		"nether_star" -> processCheckMessage(event, "Nether Stars", inventory.netherStar, inventory.maxNetherStar)
		"singularity" -> processCheckMessage(event, "Singularities", inventory.singularity, inventory.maxSingularity)
		"ultimate" -> processCheckMessage(event, "Ultimate", inventory.ultimate, inventory.maxUltimate)
		"cadrizor" -> processCheckMessage(event, "Cadriz Ore", inventory.cadrizOre, inventory.maxCadrizOre)
		"cadriz_ingot" -> processCheckMessage(event, "Cadriz", inventory.cadrizIngot, inventory.maxCadrizIngot)
		else -> Utils.sendVolatileMessage(event, ":x: Unknown resource `$name`.")
	}
}

private fun processCheckMessage(event: GuildMessageReceivedEvent, name: String, amount: Int, max: Int) {
	if (amount >= max) Utils.sendVolatileMessage(event, "$name : ${Utils.progress(max, max)}")
	else CadrizEmbedBuilder.invMissingItem(name, max).addField("Progress", Utils.progress(amount, max), true).send(event)
}