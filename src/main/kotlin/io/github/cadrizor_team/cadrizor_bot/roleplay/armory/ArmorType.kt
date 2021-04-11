package io.github.cadrizor_team.cadrizor_bot.roleplay.armory

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

enum class ArmorType(val id: String, private val nbParts: Int) {
	HELMET("helmet", 5),
	CHESTPLATE("chestplate", 8),
	LEGGINGS("leggings", 7),
	BOOTS("boots", 4),
	UNKNOWN("-", 0);

	fun getRecipeFor(tier: ArmorTier, onResult: (MemberInventory) -> Unit): Recipe? {
		val recipe = RecipeBuilder(tier.name.toLowerCase() + "_" + id, onResult)
		when (tier) {
			ArmorTier.WOODEN -> recipe.wood = NumberFactory.from(nbParts * 400)
			ArmorTier.STONE -> recipe.stone = NumberFactory.from(nbParts * 400)
			else -> return null
		}
		return recipe.build()
	}

	companion object {
		@JvmStatic
		fun fromID(id: String) = when (id) {
			"helmet" -> HELMET
			"chestplate" -> CHESTPLATE
			"leggings" -> LEGGINGS
			"boots" -> BOOTS
			else -> UNKNOWN
		}
	}
}