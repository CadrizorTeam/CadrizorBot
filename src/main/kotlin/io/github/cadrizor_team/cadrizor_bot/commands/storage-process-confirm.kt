@file:JvmName("Storage_ProcessConfirm")
package io.github.cadrizor_team.cadrizor_bot.commands

import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

fun storageProcessConfirm(event: GuildMessageReceivedEvent, inventory: MemberInventory, name: String) {
	when (name) {
		"wood" -> if (inventory.wood >= inventory.maxWood) {
			inventory.wood -= inventory.maxWood
			inventory.maxWood *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.wood, inventory.maxWood)}")
		"charcoal" -> if (inventory.charcoal >= inventory.maxCharcoal) {
			inventory.charcoal -= inventory.maxCharcoal
			inventory.maxCharcoal *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.charcoal, inventory.maxCharcoal)}")
		"coal" -> if (inventory.coal >= inventory.maxCoal) {
			inventory.coal -= inventory.maxCoal
			inventory.maxCoal *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.coal, inventory.maxCoal)}")
		"cobble" -> if (inventory.cobble >= inventory.maxCobble) {
			inventory.cobble -= inventory.maxCobble
			inventory.maxCobble *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.cobble, inventory.maxCobble)}")
		"stone" -> if (inventory.stone >= inventory.maxStone) {
			inventory.stone -= inventory.maxStone
			inventory.maxStone *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.stone, inventory.maxStone)}")
		"iron_ore" -> if (inventory.ironOre >= inventory.maxIronOre) {
			inventory.ironOre -= inventory.maxIronOre
			inventory.maxIronOre *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.ironOre, inventory.maxIronOre)}")
		"iron" -> if (inventory.ironIngot >= inventory.maxIronIngot) {
			inventory.ironIngot -= inventory.maxIronIngot
			inventory.maxIronIngot *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.ironIngot, inventory.maxIronIngot)}")
		"iron_block" -> if (inventory.ironBlock >= inventory.maxIronBlock) {
			inventory.ironBlock -= inventory.maxIronBlock
			inventory.maxIronBlock *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.ironBlock, inventory.maxIronBlock)}")
		"gold_ore" -> if (inventory.goldOre >= inventory.maxGoldOre) {
			inventory.goldOre -= inventory.maxGoldOre
			inventory.maxGoldOre *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.goldOre, inventory.maxGoldOre)}")
		"gold" -> if (inventory.goldIngot >= inventory.maxGoldIngot) {
			inventory.goldIngot -= inventory.maxGoldIngot
			inventory.maxGoldIngot *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.goldIngot, inventory.maxGoldIngot)}")
		"gold_block" -> if (inventory.goldBlock >= inventory.maxGoldBlock) {
			inventory.goldBlock -= inventory.maxGoldBlock
			inventory.maxGoldBlock *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.goldBlock, inventory.maxGoldBlock)}")
		"diamond" -> if (inventory.diamond >= inventory.maxDiamond) {
			inventory.diamond -= inventory.maxDiamond
			inventory.maxDiamond *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.diamond, inventory.maxDiamond)}")
		"diamond_block" -> if (inventory.diamondBlock >= inventory.maxDiamondBlock) {
			inventory.diamondBlock -= inventory.maxDiamondBlock
			inventory.maxDiamondBlock *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.diamondBlock, inventory.maxDiamondBlock)}")
		"emerald" -> if (inventory.emerald >= inventory.maxEmerald) {
			inventory.emerald -= inventory.maxEmerald
			inventory.maxEmerald *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.emerald, inventory.maxEmerald)}")
		"emerald_block" -> if (inventory.emeraldBlock >= inventory.maxEmeraldBlock) {
			inventory.emeraldBlock -= inventory.maxEmeraldBlock
			inventory.maxEmeraldBlock *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.emeraldBlock, inventory.maxEmeraldBlock)}")
		"diamerald" -> if (inventory.crushedDiamerald >= inventory.maxCrushedDiamerald) {
			inventory.crushedDiamerald -= inventory.maxCrushedDiamerald
			inventory.maxCrushedDiamerald *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.crushedDiamerald, inventory.maxCrushedDiamerald)}")
		"diamerald_block" -> if (inventory.crushedDiameraldBlock >= inventory.maxCrushedDiameraldBlock) {
			inventory.crushedDiameraldBlock -= inventory.maxCrushedDiameraldBlock
			inventory.maxCrushedDiameraldBlock *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.crushedDiameraldBlock, inventory.maxCrushedDiameraldBlock)}")
		"obsidian" -> if (inventory.obsidian >= inventory.maxObsidian) {
			inventory.obsidian -= inventory.maxObsidian
			inventory.maxObsidian *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.obsidian, inventory.maxObsidian)}")
		"wither_skull" -> if (inventory.witherSkulls >= inventory.maxWitherSkulls) {
			inventory.witherSkulls -= inventory.maxWitherSkulls
			inventory.maxWitherSkulls *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.witherSkulls, inventory.maxWitherSkulls)}")
		"nether_star" -> if (inventory.netherStar >= inventory.maxNetherStar) {
			inventory.netherStar -= inventory.maxNetherStar
			inventory.maxNetherStar *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.netherStar, inventory.maxNetherStar)}")
		"singularity" -> if (inventory.singularity >= inventory.maxSingularity) {
			inventory.singularity -= inventory.maxSingularity
			inventory.maxSingularity *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.singularity, inventory.maxSingularity)}")
		"ultimate" -> if (inventory.ultimate >= inventory.maxUltimate) {
			inventory.ultimate -= inventory.maxUltimate
			inventory.maxUltimate *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.ultimate, inventory.maxUltimate)}")
		"cadrizor" -> if (inventory.cadrizOre >= inventory.maxCadrizOre) {
			inventory.cadrizOre -= inventory.maxCadrizOre
			inventory.maxCadrizOre *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.cadrizOre, inventory.maxCadrizOre)}")
		"cadriz_ingot" -> if (inventory.cadrizIngot >= inventory.maxCadrizIngot) {
			inventory.cadrizIngot -= inventory.maxCadrizIngot
			inventory.maxCadrizIngot *= 2
			sendVolatileMessage(event, ":white_check_mark: Upgrade done !")
		} else sendVolatileMessage(event, ":x: $name : ${progress(inventory.cadrizIngot, inventory.maxCadrizIngot)}")
		else -> CadrizEmbedBuilder.error("Unknown resource").addField("Resource", name, true).send(event)
	}
}