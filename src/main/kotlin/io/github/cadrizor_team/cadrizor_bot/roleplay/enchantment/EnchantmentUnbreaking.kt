package io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

class EnchantmentUnbreaking : Enchantment("unbreaking", "Unbreaking", 3, *EnchantablePart.values()) {
	override val recipe: (Int) -> Recipe?
		get() = {
			RecipeBuilder("unbreaking_$it", {}, emerald = NumberFactory.from(7 * it)).build()
		}

	override fun applyEnchant(inventory: MemberInventory, part: EnchantablePart, level: Int, integer: Int /* Damage */) {
		if (part.id < 0 || part.id > 2) return
		val toAdd = (integer.toDouble() * 0.1 * level.toDouble()).toInt()
		this.apply(inventory, part, toAdd)
	}

	private fun apply(inventory: MemberInventory, part: EnchantablePart, amount: Int) {
		when (part) {
			EnchantablePart.AXE -> inventory.axeDurability += amount
			EnchantablePart.PICKAXE -> inventory.pickaxe.durability += amount
			EnchantablePart.SWORD -> inventory.swordDurability += amount
			else -> {}
		}
	}
}