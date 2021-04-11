package io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment

import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

abstract class Enchantment internal constructor(val id: String, val name: String, val maxLevel: Int, vararg applyable: EnchantablePart) {
	private val applyable = arrayOf(*applyable)
	abstract val recipe: (Int) -> Recipe?

	fun canApply(part: EnchantablePart) = applyable.any { it.id == part.id }

	abstract fun applyEnchant(inventory: MemberInventory, part: EnchantablePart, level: Int, integer: Int)

	enum class EnchantablePart(val id: Int) {
		PICKAXE(0),
		AXE(1),
		SWORD(2),

		//HELMET(3),
		//CHESTPLATE(4),
		//LEGGINGS(5),
		//BOOTS(6),
		UNKNOWN(-1);

		companion object {
			@JvmStatic
			fun byName(part: String) = try {
				valueOf(part.toUpperCase())
			} catch (exception: Exception) {
				UNKNOWN
			}
		}
	}
}