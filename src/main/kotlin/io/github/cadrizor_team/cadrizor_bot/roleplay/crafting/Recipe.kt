package io.github.cadrizor_team.cadrizor_bot.roleplay.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress

class Recipe internal constructor(
		val name: String,
		private val onCrafted: (MemberInventory) -> Unit,
		private val craftCondition: (MemberInventory) -> Boolean,
		private val wood: NumberFactory,
		private val charcoal: NumberFactory,
		private val coal: NumberFactory,
		private val cobble: NumberFactory,
		private val stone: NumberFactory,
		private val ironOre: NumberFactory,
		private val iron: NumberFactory,
		private val ironBlock: NumberFactory,
		private val goldOre: NumberFactory,
		private val gold: NumberFactory,
		private val goldBlock: NumberFactory,
		private val diamond: NumberFactory,
		private val diamondBlock: NumberFactory,
		private val emerald: NumberFactory,
		private val emeraldBlock: NumberFactory,
		private val crushedDiamerald: NumberFactory,
		private val crushedDiameraldBlock: NumberFactory,
		private val obsidian: NumberFactory,
		private val netherStar: NumberFactory,
		private val singularity: NumberFactory,
		private val ultimate: NumberFactory,
		private val cadrizOre: NumberFactory,
		private val cadrizIngot: NumberFactory
) {
	private fun cost(cost: NumberFactory, name: String, field: String, inv: MemberInventory, message: String): String {
		try {
			val method = "get${field[0].toUpperCase()}${field.substring(1)}"
			MemberInventory::class.java.getMethod(method).let {
				if (!it.isAccessible) it.isAccessible = true
				val amount = it.invoke(inv) as Int
				return if (amount >= cost.get()) "$message\n:white_check_mark: $name : `$cost`"
				else "$message\n:x: $name : ${progress(amount, cost.get())}"
			}
		} catch (e: NoSuchFieldException) {
			e.printStackTrace()
			return "Cannot check MemberInventory for field: $field"
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
			return "Cannot check MemberInventory for field: $field"
		}
	}

	fun costs(inv: MemberInventory): String {
		var message = "Crafting recipe: `$name`"
		if (!craftCondition(inv)) return "$message\n:x: Crafting condition not meet."
		if (wood.get() > 0) message = cost(wood, "Wood", "wood", inv, message)
		if (charcoal.get() > 0) message = cost(charcoal, "Charcoal", "charcoal", inv, message)
		if (coal.get() > 0) message = cost(coal, "Coal", "coal", inv, message)
		if (cobble.get() > 0) message = cost(cobble, "Cobble", "cobble", inv, message)
		if (stone.get() > 0) message = cost(stone, "Stone", "stone", inv, message)
		if (ironOre.get() > 0) message = cost(ironOre, "Iron Ore", "ironOre", inv, message)
		if (iron.get() > 0) message = cost(iron, "Iron", "ironIngot", inv, message)
		if (ironBlock.get() > 0) message = cost(ironBlock, "Iron Block", "ironBlock", inv, message)
		if (goldOre.get() > 0) message = cost(goldOre, "Gold Ore", "goldOre", inv, message)
		if (gold.get() > 0) message = cost(gold, "Gold", "goldIngot", inv, message)
		if (goldBlock.get() > 0) message = cost(goldBlock, "Gold Block", "goldBlock", inv, message)
		if (diamond.get() > 0) message = cost(diamond, "Diamond", "diamond", inv, message)
		if (diamondBlock.get() > 0) message = cost(diamondBlock, "Diamond Block", "diamondBlock", inv, message)
		if (emerald.get() > 0) message = cost(emerald, "Emerald", "emerald", inv, message)
		if (emeraldBlock.get() > 0) message = cost(emeraldBlock, "Emerald Block", "emeraldBlock", inv, message)
		if (crushedDiamerald.get() > 0)
			message = cost(crushedDiamerald, "Crushed Diamerald", "crushedDiamerald", inv, message)
		if (crushedDiameraldBlock.get() > 0)
			message = cost(crushedDiameraldBlock, "Crushed Diamerald Block", "crushedDiameraldBlock", inv, message)
		if (obsidian.get() > 0) message = cost(obsidian, "Obsidian", "obsidian", inv, message)
		if (netherStar.get() > 0) message = cost(netherStar, "Nether Stars", "netherStar", inv, message)
		if (singularity.get() > 0) message = cost(singularity, "Singularities", "singularity", inv, message)
		if (ultimate.get() > 0) message = cost(ultimate, "Ultimate", "ultimate", inv, message)
		if (cadrizOre.get() > 0) message = cost(cadrizOre, "Cadrizor", "cadrizOre", inv, message)
		if (cadrizIngot.get() > 0) message = cost(cadrizIngot, "Cadriz", "cadrizIngot", inv, message)
		return message
	}

	private fun simpleCost(cost: NumberFactory, name: String, field: String, inv: MemberInventory, message: String): String {
		return try {
			val resource = MemberInventory::class.java.getField(field)
			if (!resource.isAccessible) resource.isAccessible = true
			val amount = resource.getInt(inv)
			"$message - $name : ${progress(amount, cost.get())}"
		} catch (e: NoSuchFieldException) {
			e.printStackTrace()
			"Cannot check MemberInventory for field: $field"
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
			"Cannot check MemberInventory for field: $field"
		}
	}

	fun simpleCosts(inv: MemberInventory): String {
		var message = ""
		if (!craftCondition(inv)) return ":x: Crafting condition not meet."
		if (wood.get() > 0) message = simpleCost(wood, "Wood", "wood", inv, message)
		if (charcoal.get() > 0) message = simpleCost(charcoal, "Charcoal", "charcoal", inv, message)
		if (coal.get() > 0) message = simpleCost(coal, "Coal", "coal", inv, message)
		if (cobble.get() > 0) message = simpleCost(cobble, "Cobble", "cobble", inv, message)
		if (stone.get() > 0) message = simpleCost(stone, "Stone", "stone", inv, message)
		if (ironOre.get() > 0) message = simpleCost(ironOre, "Iron Ore", "ironOre", inv, message)
		if (iron.get() > 0) message = simpleCost(iron, "Iron", "ironIngot", inv, message)
		if (ironBlock.get() > 0) message = simpleCost(ironBlock, "Iron Block", "ironBlock", inv, message)
		if (goldOre.get() > 0) message = simpleCost(goldOre, "Gold Ore", "goldOre", inv, message)
		if (gold.get() > 0) message = simpleCost(gold, "Gold", "goldIngot", inv, message)
		if (goldBlock.get() > 0) message = simpleCost(goldBlock, "Gold Block", "goldBlock", inv, message)
		if (diamond.get() > 0) message = simpleCost(diamond, "Diamond", "diamond", inv, message)
		if (diamondBlock.get() > 0) message = simpleCost(diamondBlock, "Diamond Block", "diamondBlock", inv, message)
		if (emerald.get() > 0) message = simpleCost(emerald, "Emerald", "emerald", inv, message)
		if (emeraldBlock.get() > 0) message = simpleCost(emeraldBlock, "Emerald Block", "emeraldBlock", inv, message)
		if (crushedDiamerald.get() > 0)
			message = simpleCost(crushedDiamerald, "Crushed Diamerald", "crushedDiamerald", inv, message)
		if (crushedDiameraldBlock.get() > 0)
			message = simpleCost(crushedDiameraldBlock, "Crushed Diamerald Block", "crushedDiameraldBlock", inv, message)
		if (obsidian.get() > 0) message = simpleCost(obsidian, "Obsidian", "obsidian", inv, message)
		if (netherStar.get() > 0) message = simpleCost(netherStar, "Nether Stars", "netherStar", inv, message)
		if (singularity.get() > 0) message = simpleCost(singularity, "Singularities", "singularity", inv, message)
		if (ultimate.get() > 0) message = simpleCost(ultimate, "Ultimate", "ultimate", inv, message)
		if (cadrizOre.get() > 0) message = simpleCost(cadrizOre, "Cadrizor", "cadrizOre", inv, message)
		if (cadrizIngot.get() > 0) message = simpleCost(cadrizIngot, "Cadriz", "cadrizIngot", inv, message)
		return message
	}

	fun craft(inv: MemberInventory) = if (!canCraft(inv) || !craftCondition(inv)) false else processCraft(inv)

	fun canCraft(inv: MemberInventory) = craftCondition(inv) && inv.wood >= wood.get()
			&& inv.charcoal >= charcoal.get() && inv.coal >= coal.get() && inv.cobble >= cobble.get()
			&& inv.stone >= stone.get() && inv.ironOre >= ironOre.get() && inv.ironIngot >= iron.get()
			&& inv.ironBlock >= ironBlock.get() && inv.goldOre >= goldOre.get() && inv.goldIngot >= gold.get()
			&& inv.goldBlock >= goldBlock.get() && inv.diamond >= diamond.get() && inv.diamondBlock >= diamondBlock.get()
			&& inv.emerald >= emerald.get() && inv.emeraldBlock >= emeraldBlock.get()
			&& inv.crushedDiamerald >= crushedDiamerald.get() && inv.crushedDiameraldBlock >= crushedDiameraldBlock.get()
			&& inv.obsidian >= obsidian.get() && inv.netherStar >= netherStar.get() && inv.singularity >= singularity.get()
			&& inv.ultimate >= ultimate.get() && inv.cadrizOre >= cadrizOre.get() && inv.cadrizIngot >= cadrizIngot.get()

	private fun processCraft(inv: MemberInventory): Boolean {
		inv.wood -= wood.get()
		inv.charcoal -= charcoal.get()
		inv.coal -= coal.get()
		inv.cobble -= cobble.get()
		inv.stone -= stone.get()
		inv.ironOre -= ironOre.get()
		inv.ironIngot -= iron.get()
		inv.ironBlock -= ironBlock.get()
		inv.goldOre -= goldOre.get()
		inv.goldIngot -= gold.get()
		inv.goldBlock -= goldBlock.get()
		inv.diamond -= diamond.get()
		inv.diamondBlock -= diamondBlock.get()
		inv.emerald -= emerald.get()
		inv.emeraldBlock -= emeraldBlock.get()
		inv.crushedDiamerald -= crushedDiamerald.get()
		inv.crushedDiameraldBlock -= crushedDiameraldBlock.get()
		inv.obsidian -= obsidian.get()
		inv.netherStar -= netherStar.get()
		inv.singularity -= singularity.get()
		inv.ultimate -= ultimate.get()
		inv.cadrizOre -= cadrizOre.get()
		inv.cadrizIngot -= cadrizIngot.get()
		onCrafted(inv)
		return true
	}
}