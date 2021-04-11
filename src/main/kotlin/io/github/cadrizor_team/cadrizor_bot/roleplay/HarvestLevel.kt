package io.github.cadrizor_team.cadrizor_bot.roleplay

enum class HarvestLevel(val levelName: String, val durability: Int) {
	HAND("with your hand", Integer.MIN_VALUE),
	GOLD("gold", 74),
	WOOD("wooden", 149),
	STONE("stone", 299),
	IRON("iron", 499),
	DIAMOND("diamonds", 1999),
	EMERALD("emeralds", 2999),
	DIAMERALD("made with Diamerald Alloy", 4999),
	NETHER_STARS("made with Nether Stars", 9999),
	SINGULARITY("made with Singularities", 19999),
	ULTIMATE("ultimate", 49999),
	CADRIZOR("with Cadriz **Ores**", 99999),
	CADRIZ("with Cadriz **Ingots**", -1);

	override fun toString(): String = levelName

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
}