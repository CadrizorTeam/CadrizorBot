package io.github.cadrizor_team.cadrizor_bot.init.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.SmeltingLevel

object FunctionnalCraftingInit {
	fun init() {
		RecipeBuilder(
				name = "furnace",
				onCraft = { it.furnaceLevel = SmeltingLevel.COBBLE },
				condition = { it.furnaceLevel == SmeltingLevel.NONE },
				cobble = from(600)
		).register()

		RecipeBuilder(
				name = "armorbench",
				onCraft = { it.armorbench },
				condition = { !it.armorbench },
				wood = NumberFactory(k = 1),
				cobble = NumberFactory(k = 1),
				iron = from(750),
				gold = from(600)
		).register()

		RecipeBuilder(
				name = "enchantbench",
				onCraft = { it.enchantbench },
				condition = { !it.enchantbench },
				diamond = NumberFactory(u = 800),
				obsidian = from(1250)
		).register()
	}
}