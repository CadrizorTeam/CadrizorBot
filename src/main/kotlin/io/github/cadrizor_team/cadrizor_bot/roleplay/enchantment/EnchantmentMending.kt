package io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment

import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.RecipeBuilder
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import kotlin.math.min
import kotlin.math.max

class EnchantmentMending : Enchantment("mending", "Mending", 1, *EnchantablePart.values()) {
	override val recipe: (Int) -> Recipe?
		get() = {
			RecipeBuilder("mending", {}, emerald = NumberFactory.from(25)).build()
		}

	override fun applyEnchant(inventory: MemberInventory, part: EnchantablePart, level: Int /* = 1 */, integer: Int /* XP */) {
		val sword = inventory.swordLevel
		val axe = inventory.axeHarvLvl

		val swordMax = sword.durability
		val swordDurability = inventory.swordDurability
		val axeMax = axe.durability
		val axeDurability = inventory.axeDurability

		var xpConsume = 0
		var durability: Int

		if (swordDurability != 1 && swordDurability != swordMax) {
			durability = min(swordMax - swordDurability, integer)
			inventory.swordDurability += durability
			xpConsume += durability
		}

		for (i in inventory.pickaxes.indices) {
			val memberPickaxe = inventory.pickaxes[i]
			val pickaxeMax = memberPickaxe.miningLevel.durability
			if (memberPickaxe.durability != 1 && memberPickaxe.durability < pickaxeMax) {
				durability = min(pickaxeMax - memberPickaxe.durability, integer)
				memberPickaxe.durability += durability
				xpConsume += durability
			}
			inventory.pickaxes[i] = memberPickaxe
		}

		if (axeDurability != 1 && axeDurability != axeMax) {
			durability = min(axeMax - axeDurability, integer)
			inventory.axeDurability += durability
			xpConsume += durability
		}

		inventory.xp.addXp(max(integer - xpConsume, 0))
	}
}