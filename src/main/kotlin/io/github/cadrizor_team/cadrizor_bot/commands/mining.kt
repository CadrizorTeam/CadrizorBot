package io.github.cadrizor_team.cadrizor_bot.commands

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.lang.BooleanHashMap
import io.github.cadrizor_team.cadrizor_bot.lang.setIfNull
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberPickaxe
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

typealias CollectMine = (MemberInventory, MemberPickaxe, Int, Boolean) -> Unit

private fun getMining(
		name: String, maxMining: Int, maxBypass: Int, inventory: MemberInventory,
		pickaxe: MemberPickaxe, mapWin: BooleanHashMap, collect: CollectMine
) {
	var amount = Random.nextInt(maxMining)
	if (pickaxe.durability != -1 && amount > pickaxe.durability) amount = pickaxe.durability
	if (maxBypass > 0 && !Bonuses.bypassMiningLimit && amount > 20) amount = 20
	val amountBonus = (amount * Bonuses.bonusMiner).toInt()
	mapWin.map[name] = NumberFactory.from(amountBonus)
	collect.invoke(inventory, pickaxe, amountBonus, Bonuses.allowsOverstorage)
	pickaxe.durability -= amount
	if (pickaxe.durability == 0) mapWin.setFalse()
	inventory.pickaxe = pickaxe
	mapWin.map["TotalMined"] = NumberFactory.from(
			(mapWin.map["TotalMined"] ?: NumberFactory()).get() + amountBonus
	)
}

internal fun applyMining(inventory: MemberInventory): BooleanHashMap {
	val pickaxe = inventory.pickaxe
	val paxeLevel = pickaxe.miningLevel
	val harvestLevel = paxeLevel.harvestLevel()
	val fortunes = floatArrayOf(1.0f, 1.2f, 1.5f, 2.0f)
	val mapWin = BooleanHashMap(true)
	if (harvestLevel >= 0) {
		getMining("Cobble", 10 * (paxeLevel.level() + 1), 20,
				inventory, pickaxe, mapWin) { inv, _, amount, overstorage ->
			if (overstorage) inv.cobble += amount
			else inv.cobble = min(inv.cobble + amount, inv.maxCobble)
		}
		if (!mapWin.boolean) return mapWin
	}
	if (harvestLevel >= 1) {
		getMining("Iron ${if (pickaxe.hasSmelting) "Ingot" else "Ore"}", 8 * (paxeLevel.level() + 1), 25,
				inventory, pickaxe, mapWin) { inv, pick, amount, overstorage ->
			if (pick.hasSmelting) {
				if (overstorage) inv.ironIngot += amount
				else inv.ironIngot = min(inv.ironIngot + amount, inv.maxIronIngot)
			} else {
				if (overstorage) inv.ironOre += amount
				else inv.ironOre = min(inv.ironOre + amount, inv.maxIronOre)
			}
		}
		if (!mapWin.boolean) return mapWin

		getMining("Gold ${if (pickaxe.hasSmelting) "Ingot" else "Ore"}", 5 * (paxeLevel.level() + 1), 25,
				inventory, pickaxe, mapWin) { inv, pick, amount, overstorage ->
			if (pick.hasSmelting) {
				if (overstorage) inv.goldIngot += amount
				else inv.goldIngot = min(inv.goldIngot + amount, inv.maxGoldIngot)
			} else {
				if (overstorage) inv.goldOre += amount
				else inv.goldOre = min(inv.goldOre + amount, inv.maxGoldOre)
			}
		}
		if (!mapWin.boolean) return mapWin
	}
	if (harvestLevel >= 2) {
		getMining("Diamond${if (pickaxe.isSilk) " Ore" else ""}", 2 * (paxeLevel.level() + 1), -1,
				inventory, pickaxe, mapWin) { inv, pick, amount, overstorage ->
			if (pick.isSilk) {
				if (overstorage) inv.diamondOre += amount
				else inv.diamondOre = min(inv.diamondOre + amount, inv.maxDiamondOre)
			} else {
				val fortune1 = fortunes[pick.fortuneLevel]
				pickaxe.durability -= (amount * fortune1 - amount).roundToInt()
				if (overstorage) inv.diamond += floor(amount * fortune1).toInt()
				else inv.diamond = min((inv.diamond + amount * fortune1).toInt(), inv.maxDiamond)
			}
		}
		if (!mapWin.boolean) return mapWin

		getMining("Emerald${if (pickaxe.isSilk) " Ore" else ""}", paxeLevel.level() + 1, -1,
				inventory, pickaxe, mapWin) { inv, pick, amount, overstorage ->
			if (pick.isSilk) {
				if (overstorage) inv.emeraldOre += amount
				else inv.emeraldOre = min(inv.emeraldOre + amount, inv.maxEmeraldOre)
			} else {
				val fortune1 = fortunes[pick.fortuneLevel]
				pickaxe.durability -= (amount * fortune1 - amount).roundToInt()
				if (overstorage) inv.emerald += floor(amount * fortune1).toInt()
				else inv.emerald = min((inv.emerald + amount * fortune1).toInt(), inv.maxEmerald)
			}
		}
		if (!mapWin.boolean) return mapWin
	}
	if (harvestLevel >= 3) {
		getMining("Obsidian", paxeLevel.level(), -1,
				inventory, pickaxe, mapWin) { inv, _, amount, overstorage ->
			if (overstorage) inv.obsidian += amount
			else inv.obsidian = min(inv.obsidian + amount, inv.maxObsidian)
		}
		if (!mapWin.boolean) return mapWin
	}
	if (harvestLevel >= 4) {
		val stars = Random.nextInt(100)
		if (((pickaxe.durability == -1 || pickaxe.durability >= 5)
						&& (Bonuses.allowsOverstorage || stars < inventory.maxNetherStar)
						&& stars > (98 - pickaxe.fortuneLevel - (paxeLevel.level() - 5)))) {
			mapWin.map["Nether Stars"] = NumberFactory.from(1)
			inventory.netherStar++
			pickaxe.durability -= 5
		}

		mapWin.map.setIfNull("TotalMined", NumberFactory())
		mapWin.map["TotalMined"]!!.add(1)
		if (pickaxe.isBroken) return mapWin.setFalse()
	}
	if (pickaxe.enchants.containsKey(EnchantmentsInit.UNBREAKING)) {
		val level = pickaxe.enchants[EnchantmentsInit.UNBREAKING] ?: 0
		EnchantmentsInit.UNBREAKING.applyEnchant(inventory, Enchantment.EnchantablePart.PICKAXE, level, mapWin.map["TotalMined"]?.get() ?: 0)
	}
	return mapWin
}