package io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment

import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

class EnchantmentSmelting : Enchantment("smelting", "Smelting", 1, EnchantablePart.AXE, EnchantablePart.PICKAXE) {
	override val recipe: (Int) -> Recipe?
		get() = {
			RecipeBuilder("smelting", {}).build()
			null
		}

	override fun applyEnchant(inventory: MemberInventory, part: EnchantablePart, level: Int, integer: Int) {
		// Apply Enchant on `${Main.prefix}mine` command
	}
}