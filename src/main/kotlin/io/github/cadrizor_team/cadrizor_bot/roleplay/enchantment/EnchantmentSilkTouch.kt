package io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

class EnchantmentSilkTouch : Enchantment("silk_touch", "Silk Touch", 1, EnchantablePart.AXE, EnchantablePart.PICKAXE) {
	override val recipe: (Int) -> Recipe?
		get() = {
			RecipeBuilder("silk_touch", {}, emerald = NumberFactory.from(35)).build()
		}

	override fun applyEnchant(inventory: MemberInventory, part: EnchantablePart, level: Int, integer: Int) {
		// Apply Enchant on `${Main.prefix}mine` command
	}
}