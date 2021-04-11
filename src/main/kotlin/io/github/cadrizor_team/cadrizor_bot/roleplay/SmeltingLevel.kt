package io.github.cadrizor_team.cadrizor_bot.roleplay

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder

enum class SmeltingLevel(val smeltingResult: Int, val maxInput: Int, val nextTierRecipe: Recipe?) {
	NONE(0, 0, null),
	COBBLE(
			1,
			25,
			RecipeBuilder(
					name = "Stone Furnace",
					onCraft = { it.furnaceLevel = STONE },
					condition = { it.furnaceLevel == COBBLE },
					stone = from(250)
			).build()),
	STONE(
			1,
			40,
			RecipeBuilder(
					name = "Iron Furnace",
					onCraft = { it.furnaceLevel = IRON },
					condition = { it.furnaceLevel == STONE },
					iron = from(300)
			).build()
	),
	IRON(1, 60, null),
	GOLD(1, 65, null),
	DIAMOND(2, 90, null),
	EMERALD(2, 95, null),
	DIAMERALD(2, 110, null),
	NETHER_STARS(2, 140, null),
	SINGULARITY(3, 180, null),
	ULTIMATE(3, 250, null),
	CADRIZOR(4, 400, null),
	CADRIZ(4, 600, null);
}