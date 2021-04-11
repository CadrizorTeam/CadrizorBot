package io.github.cadrizor_team.cadrizor_bot.init.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder

object BlockRecipesInit {
	private const val INGOTS_TO_BLOCK = 42

	fun init() {
		RecipeBuilder(
				name = "iron_block",
				onCraft = { it.ironBlock += 1 },
				iron = from(INGOTS_TO_BLOCK)
		).register()

		RecipeBuilder(
				name = "gold_block",
				onCraft = { it.goldBlock += 1 },
				gold = from(INGOTS_TO_BLOCK)
		).register()

		RecipeBuilder(
				name = "diamond_block",
				onCraft = { it.diamondBlock += 1 },
				diamond = from(INGOTS_TO_BLOCK)
		).register()

		RecipeBuilder(
				name = "emerald_block",
				onCraft = { it.emeraldBlock += 1 },
				emerald = from(INGOTS_TO_BLOCK)
		).register()

		RecipeBuilder(
				name = "diamerald_block",
				onCraft = { it.crushedDiameraldBlock += 1 },
				crushedDiamerald = from(INGOTS_TO_BLOCK * 10)
		).register()
	}
}