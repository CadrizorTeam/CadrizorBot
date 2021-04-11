package io.github.cadrizor_team.cadrizor_bot.init.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel.*
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

object PickaxeRecipesInit {
	@JvmStatic
	fun init() {
		RecipeBuilder(
				name = "pick_wood",
				onCraft = {
					it.pickaxe.upgrade(it, WOOD)
				},
				condition = { it.pickaxe.miningLevel == HAND || it.pickaxe.miningLevel == WOOD },
				wood = from(100)
		).register()

		val cobbleStoneCondition: (MemberInventory) -> Boolean = {
			val miningLevel = it.pickaxe.miningLevel
			miningLevel == HAND || miningLevel == WOOD || miningLevel == STONE
		}

		RecipeBuilder(
				name = "pick_cobble",
				onCraft = {
					it.pickaxe.upgrade(it, STONE)
				},
				condition = cobbleStoneCondition,
				wood = from(25),
				cobble = from(75)
		).register()

		RecipeBuilder(
				name = "pick_stone",
				onCraft = {
					it.pickaxe.upgrade(it, STONE)
				},
				condition = cobbleStoneCondition,
				wood = from(25),
				stone = from(75)
		).register()

		RecipeBuilder(
				name = "pick_iron",
				onCraft = {
					it.pickaxe.upgrade(it, IRON)
				},
				condition = { it.pickaxe.miningLevel == STONE || it.pickaxe.miningLevel == IRON },
				wood = from(25),
				iron = from(75)
		).register()

		RecipeBuilder(
				name = "pick_diamond",
				onCraft = {
					it.pickaxe.upgrade(it, DIAMOND)
				},
				condition = { it.pickaxe.miningLevel == IRON || it.pickaxe.miningLevel == DIAMOND },
				wood = from(25),
				iron = from(50),
				diamond = from(75)
		).register()

		RecipeBuilder(
				name = "pick_emerald",
				onCraft = {
					it.pickaxe.upgrade(it, EMERALD)
				},
				condition = { it.pickaxe.miningLevel == DIAMOND || it.pickaxe.miningLevel == EMERALD },
				wood = from(25),
				diamond = from(50),
				emerald = from(75)
		).register()

		RecipeBuilder(
				name = "pick_diamerald",
				onCraft = {
					it.pickaxe.upgrade(it, DIAMERALD)
				},
				condition = { it.pickaxe.miningLevel == EMERALD || it.pickaxe.miningLevel == DIAMERALD },
				iron = from(30),
				crushedDiamerald = from(50)
		).register()

		RecipeBuilder(
				name = "axe_nether",
				onCraft = {
					it.pickaxe.upgrade(it, NETHER_STARS)
				},
				condition = { it.pickaxe.miningLevel == DIAMERALD || it.pickaxe.miningLevel == NETHER_STARS },
				iron = from(20),
				crushedDiamerald = from(35),
				netherStar = from(15)
		).register()
	}
}