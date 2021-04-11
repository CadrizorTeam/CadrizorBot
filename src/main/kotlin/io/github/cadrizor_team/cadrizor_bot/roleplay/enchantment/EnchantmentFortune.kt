package io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

class EnchantmentFortune : Enchantment("fortune", "Fortune", 3, EnchantablePart.PICKAXE) {
	override val recipe: (Int) -> Recipe?
		get() = {
			RecipeBuilder("fortune_$it", {}, emerald = NumberFactory.from(5 * it + if (it % 2 == 0) 1 else 0)).build()
		}

	override fun applyEnchant(inventory: MemberInventory, part: EnchantablePart, level: Int, integer: Int) {
		// Apply Enchant on `${Main.prefix}mine` command
	}
}