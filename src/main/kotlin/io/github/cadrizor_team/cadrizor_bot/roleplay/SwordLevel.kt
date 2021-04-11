package io.github.cadrizor_team.cadrizor_bot.roleplay

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder

enum class SwordLevel(private val levelName: String, val durability: Int, val damage: Float, val nextRecipe: Recipe?) {
	HAND(
			"with your hand",
			Integer.MIN_VALUE,
			0.5f,
			RecipeBuilder(
					name = "Wooden Sword",
					onCraft = {
						it.swordLevel = WOOD
						it.swordDurability = WOOD.durability
					},
					wood = from(600)
			).build()
	),
	GOLD("gold", 74, 1.5f, null),
	WOOD(
			"wooden",
			149,
			1.0f,
			RecipeBuilder(
					name = "Stone Sword",
					onCraft = {
						it.swordLevel = STONE
						it.swordDurability = STONE.durability
					},
					condition = { it.swordLevel == WOOD },
					cobble = from(600)
			).build()
	),
	STONE(
			"stone",
			299,
			2.0f,
			RecipeBuilder(
					name = "Iron Sword",
					onCraft = {
						it.swordLevel = IRON
						it.swordDurability = IRON.durability
					},
					condition = { it.swordLevel == STONE },
					iron = from(550)
			).build()
	),
	IRON(
			"iron",
			499,
			3.5f,
			RecipeBuilder(
					name = "Diamond Sword",
					onCraft = {
						it.swordLevel = DIAMOND
						it.swordDurability = DIAMOND.durability
					},
					condition = { it.swordLevel == IRON },
					diamond = from(500)
			).build()
	),
	DIAMOND("diamonds", 1999, 5.5f, null),
	EMERALD("emeralds", 2999, 6.0f, null),
	DIAMERALD("made with Diamerald Alloy", 4999, 7.5f, null),
	NETHER_STARS("made with Nether Stars", 9999, 9.0f, null),
	SINGULARITY("made with Singularities", 19999, 11.0f, null),
	ULTIMATE("ultimate", 49999, 12.0f, null),
	CADRIZOR("with Cadriz **Ores**", 99999, 13.0f, null),
	CADRIZ("with Cadriz **Ingots**", -1, 14.0f, null);

	val next: SwordLevel?
		get() = when (this) {
			HAND -> WOOD
			WOOD -> STONE
			STONE -> IRON
			IRON -> DIAMOND
			DIAMOND -> EMERALD
			EMERALD -> DIAMERALD
			DIAMERALD -> NETHER_STARS
			NETHER_STARS -> SINGULARITY
			SINGULARITY -> ULTIMATE
			ULTIMATE -> CADRIZOR
			CADRIZOR -> CADRIZ
			else -> null
		}

	override fun toString() = levelName

	fun level() = when (this) {
		HAND -> 0
		WOOD, GOLD -> 1
		STONE -> 2
		IRON -> 3
		DIAMOND, EMERALD -> 4
		DIAMERALD -> 5
		NETHER_STARS -> 6
		SINGULARITY -> 7
		ULTIMATE -> 8
		CADRIZOR, CADRIZ -> 9
	}

	fun harvestLevel() = level() - 1

	operator fun hasNext() = this != EMERALD && next != null
}