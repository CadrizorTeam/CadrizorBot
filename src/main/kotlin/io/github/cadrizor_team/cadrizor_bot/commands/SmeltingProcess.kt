package io.github.cadrizor_team.cadrizor_bot.commands

import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import kotlin.math.min

object SmeltingProcess {
	internal fun defineTimes(minv: MemberInventory, choice: String, nb_times: Int, maxInput: Int): Int {
		val times = min(nb_times, maxInput)
		return when (choice) {
			"wood" -> min(times, minv.wood)
			"cobble" -> min(times, minv.cobble)
			"iron_ore" -> min(times, minv.ironOre)
			"gold_ore" -> min(times, minv.goldOre)
			"diamond_ore" -> min(times, minv.diamondOre)
			"emerald_ore" -> min(times, minv.emeraldOre)
			"cadriz_ore" -> min(times, minv.cadrizOre)
			else -> times
		}
	}

	internal fun doSmelting(minv: MemberInventory, choice: String, times: Int): String {
		val level = minv.furnaceLevel
		val choices = object : HashMap<String, String>() {
			init {
				this["wood"] = "Charcoal"
				this["cobble"] = "Stone"
				this["iron_ore"] = "Iron Ingot"
				this["gold_ore"] = "Gold Ingot"
				this["diamond_ore"] = "Diamond Ingot"
				this["emerald_ore"] = "Emerald Ingot"
				this["cadriz_ore"] = "Cadriz Ingot"
			}
		}
		var message = "You smelt $times"
		when (choice) {
			"wood" -> if (minv.wood >= times && minv.charcoal <= minv.maxCharcoal - level.smeltingResult * times) {
				minv.wood -= times
				minv.charcoal += level.smeltingResult * times
				message += " Wood"
			}
			"cobble" -> if (minv.cobble >= times && minv.stone <= minv.maxStone - level.smeltingResult * times) {
				minv.cobble -= times
				minv.stone += level.smeltingResult * times
				message += " Cobble"
			}
			"iron_ore" -> if (minv.ironOre >= times && minv.ironIngot <= minv.maxIronIngot - level.smeltingResult * times) {
				minv.ironOre -= times
				minv.ironIngot += level.smeltingResult * times
				message += " Iron Ore"
			}
			"gold_ore" -> if (minv.goldOre >= times && minv.goldIngot <= minv.maxGoldIngot - level.smeltingResult * times) {
				minv.goldOre -= times
				minv.goldIngot += level.smeltingResult * times
				message += " Gold Ore"
			}
			"diamond_ore" -> if (minv.diamondOre >= times && minv.diamond <= minv.maxDiamond - level.smeltingResult * times) {
				minv.diamondOre -= times
				minv.diamond += level.smeltingResult * times
				message += " Diamond"
			}
			"emerald_ore" -> if (minv.emeraldOre >= times && minv.emerald <= minv.maxEmerald - level.smeltingResult * times) {
				minv.emeraldOre -= times
				minv.emerald += level.smeltingResult * times
				message += " Emerald"
			}
			"cadriz_ore" -> if (minv.cadrizOre >= times && minv.cadrizIngot <= minv.maxCadrizIngot - level.smeltingResult * times) {
				minv.cadrizOre -= times
				minv.cadrizIngot += level.smeltingResult * times
				message += " Cadrizor"
			}
		}
		return "$message to have ${level.smeltingResult * times} ${choices[choice]}"
	}
}