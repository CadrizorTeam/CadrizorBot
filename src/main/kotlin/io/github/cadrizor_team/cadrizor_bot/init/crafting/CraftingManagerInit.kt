package io.github.cadrizor_team.cadrizor_bot.init.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder

object CraftingManagerInit {
	fun init() {
		BlockRecipesInit.init()

		AxeRecipesInit.init()
		PickaxeRecipesInit.init()

		FunctionnalCraftingInit.init()

		RecipeBuilder(
				name = "diamerald",
				onCraft = { it.crushedDiamerald += 2 },
				diamond = from(20),
				emerald = from(10)
		).register()

		RecipeBuilder(
				name = "singularity",
				onCraft = { it.singularity += 1 },
				wood = NumberFactory(k = 30),
				cobble = NumberFactory(k = 30),
				stone = NumberFactory(k = 30),
				iron = NumberFactory(k = 5),
				ironBlock = NumberFactory(k = 15),
				gold = NumberFactory(k = 5),
				goldBlock = NumberFactory(k = 15),
				diamondBlock = NumberFactory(k = 15),
				emeraldBlock = NumberFactory(k = 15),
				crushedDiamerald = NumberFactory(k = 10),
				crushedDiameraldBlock = NumberFactory(u = 100),
				obsidian = from(4500),
				netherStar = from(1750)
		).register()

		RecipeBuilder(
				name = "ultimate",
				onCraft = { it.ultimate += 1 },
				singularity = NumberFactory(k = 40)
		).register()

		RecipeBuilder(
				name = "cadrizor",
				onCraft = { it.cadrizOre += 1 },
				ultimate = NumberFactory(k = 70)
		).register()
	}
}