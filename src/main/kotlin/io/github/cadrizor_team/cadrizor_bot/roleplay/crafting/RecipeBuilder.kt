package io.github.cadrizor_team.cadrizor_bot.roleplay.crafting

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

class RecipeBuilder(
		private val name: String,
		private val onCraft: (MemberInventory) -> Unit,
		var condition: (MemberInventory) -> Boolean = { true },
		var wood: NumberFactory = NumberFactory(),
		var charcoal: NumberFactory = NumberFactory(),
		var coal: NumberFactory = NumberFactory(),
		var cobble: NumberFactory = NumberFactory(),
		var stone: NumberFactory = NumberFactory(),
		var ironOre: NumberFactory = NumberFactory(),
		var iron: NumberFactory = NumberFactory(),
		var ironBlock: NumberFactory = NumberFactory(),
		var goldOre: NumberFactory = NumberFactory(),
		var gold: NumberFactory = NumberFactory(),
		var goldBlock: NumberFactory = NumberFactory(),
		var diamond: NumberFactory = NumberFactory(),
		var diamondBlock: NumberFactory = NumberFactory(),
		var emerald: NumberFactory = NumberFactory(),
		var emeraldBlock: NumberFactory = NumberFactory(),
		var crushedDiamerald: NumberFactory = NumberFactory(),
		var crushedDiameraldBlock: NumberFactory = NumberFactory(),
		var obsidian: NumberFactory = NumberFactory(),
		var netherStar: NumberFactory = NumberFactory(),
		var singularity: NumberFactory = NumberFactory(),
		var ultimate: NumberFactory = NumberFactory(),
		var cadrizOre: NumberFactory = NumberFactory(),
		var cadrizIngot: NumberFactory = NumberFactory()
) {
	fun build() = Recipe(name, onCraft, condition, wood, charcoal, coal, cobble, stone, ironOre, iron, ironBlock, goldOre, gold, goldBlock, diamond, diamondBlock, emerald, emeraldBlock, crushedDiamerald, crushedDiameraldBlock, obsidian, netherStar, singularity, ultimate, cadrizOre, cadrizIngot)
	fun register() = CraftingManager.register(build())
}