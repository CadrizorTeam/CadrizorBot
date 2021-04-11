package io.github.cadrizor_team.cadrizor_bot.init.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel.*
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability

object AxeRecipesInit {
	@JvmStatic
	fun init() {
		RecipeBuilder(
				name = "axe_wood",
				onCraft = {
					it.axeDurability = getDurability(WOOD.durability, it)
					it.axeHarvLvl = WOOD
				},
				condition = { it.axeHarvLvl == HAND || it.axeHarvLvl == WOOD },
				wood = from(100)
		).register()

		val cobbleStoneCondition: (MemberInventory) -> Boolean = { it.axeHarvLvl == HAND || it.axeHarvLvl == WOOD || it.axeHarvLvl == STONE }

		RecipeBuilder(
				name = "axe_cobble",
				onCraft = {
					it.axeDurability = getDurability(STONE.durability, it)
					it.axeHarvLvl = STONE
				},
				condition = cobbleStoneCondition,
				wood = from(25),
				cobble = from(75)
		).register()

		RecipeBuilder(
				name = "axe_stone",
				onCraft = {
					it.axeDurability = getDurability(STONE.durability, it)
					it.axeHarvLvl = STONE
				},
				condition = cobbleStoneCondition,
				wood = from(25),
				stone = from(75)
		).register()

		RecipeBuilder(
				name = "axe_iron",
				onCraft = {
					it.axeDurability = getDurability(IRON.durability, it)
					it.axeHarvLvl = IRON
				},
				condition = { it.axeHarvLvl == STONE || it.axeHarvLvl == IRON },
				wood = from(25),
				iron = from(75)
		).register()

		RecipeBuilder(
				name = "axe_diamond",
				onCraft = {
					it.axeDurability = getDurability(DIAMOND.durability, it)
					it.axeHarvLvl = DIAMOND
				},
				condition = { it.axeHarvLvl == IRON || it.axeHarvLvl == DIAMOND },
				wood = from(25),
				iron = from(50),
				diamond = from(75)
		).register()

		RecipeBuilder(
				name = "axe_emerald",
				onCraft = {
					it.axeDurability = getDurability(EMERALD.durability, it)
					it.axeHarvLvl = EMERALD
				},
				condition = { it.axeHarvLvl == DIAMOND || it.axeHarvLvl == EMERALD },
				wood = from(25),
				diamond = from(50),
				emerald = from(75)
		).register()

		RecipeBuilder(
				name = "axe_diamerald",
				onCraft = {
					it.axeDurability = getDurability(DIAMERALD.durability, it)
					it.axeHarvLvl = DIAMERALD
				},
				condition = { it.axeHarvLvl == EMERALD || it.axeHarvLvl == DIAMERALD },
				iron = from(30),
				crushedDiamerald = from(50)
		).register()

		RecipeBuilder(
				name = "axe_nether",
				onCraft = {
					it.axeDurability = getDurability(DIAMERALD.durability, it)
					it.axeHarvLvl = DIAMERALD
				},
				condition = { it.axeHarvLvl == DIAMERALD || it.axeHarvLvl == NETHER_STARS },
				iron = from(20),
				crushedDiamerald = from(35),
				netherStar = from(15)
		).register()
	}
}