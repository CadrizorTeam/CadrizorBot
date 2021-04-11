@file:JvmName("ForceSmelt")

package io.github.cadrizor_team.cadrizor_bot.admin

import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import net.dv8tion.jda.api.entities.Member

fun smeltAll(member: Member, inventory: MemberInventory) {
	inventory.charcoal += inventory.wood
	inventory.stone += inventory.cobble
	inventory.ironIngot += inventory.ironOre
	inventory.goldIngot += inventory.goldOre
	inventory.diamond += inventory.diamondOre
	inventory.emerald += inventory.emeraldOre
	inventory.cadrizIngot += inventory.cadrizOre

	inventory.wood = 0
	inventory.cobble = 0
	inventory.ironOre = 0
	inventory.goldOre = 0
	inventory.diamondOre = 0
	inventory.emeraldOre = 0
	inventory.cadrizOre = 0

	inventory.storeData(member)
}

fun smeltType(member: Member, inventory: MemberInventory, type: String) {
	when (type) {
		"wood" -> {
			inventory.charcoal += inventory.wood
			inventory.wood = 0
		}
		"cobblestone" -> {
			inventory.stone += inventory.cobble
			inventory.cobble = 0
		}
		"iron_ore" -> {
			inventory.ironIngot += inventory.ironOre
			inventory.ironOre = 0
		}
		"gold_ore" -> {
			inventory.goldIngot += inventory.goldOre
			inventory.goldOre = 0
		}
		"diamond_ore" -> {
			inventory.diamond += inventory.diamondOre
			inventory.diamondOre = 0
		}
		"emerald_ore" -> {
			inventory.emerald += inventory.emeraldOre
			inventory.emeraldOre = 0
		}
		"cadriz_ore" -> {
			inventory.cadrizIngot += inventory.cadrizOre
			inventory.cadrizOre = 0
		}
	}

	inventory.storeData(member)
}