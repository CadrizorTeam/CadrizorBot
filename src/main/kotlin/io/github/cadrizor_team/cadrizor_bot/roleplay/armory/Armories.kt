package io.github.cadrizor_team.cadrizor_bot.roleplay.armory

import com.flowpowered.nbt.IntArrayTag
import io.github.cadrizor_team.cadrizor_bot.roleplay.crafting.Recipe

open class Armories(val type: ArmorType, val tier: ArmorTier) {
	class Helmet(tier: ArmorTier, var damage: Int, var maxDamage: Int): Armories(ArmorType.HELMET, tier) {
		val ints: IntArrayTag
			get() = IntArrayTag("stats", intArrayOf(tier.protection, damage, maxDamage))

		val craftingRecipe: Recipe?
			get() = if (next() != null) ArmorType.HELMET.getRecipeFor(next()!!.tier) { mi -> mi.armor.helmet = next()!! } else null

		operator fun next() = when {
			equals(NONE) -> WOODEN
			equals(WOODEN) -> STONE
			else -> null
		}

		override fun equals(other: Any?) = other is Helmet && tier == other.tier

		companion object {
			var NONE = Helmet(ArmorTier.NONE, 0, 0)
			var WOODEN = Helmet(ArmorTier.WOODEN, 100, 100)
			var STONE = Helmet(ArmorTier.STONE, 250, 250)
		}
	}

	class Chestplate(tier: ArmorTier, var damage: Int, var maxDamage: Int): Armories(ArmorType.CHESTPLATE, tier) {
		val ints: IntArrayTag
			get() = IntArrayTag("stats", intArrayOf(tier.protection, damage, maxDamage))

		val craftingRecipe: Recipe?
			get() = if (next() != null) ArmorType.CHESTPLATE.getRecipeFor(next()!!.tier) { mi -> mi.armor.chestplate = next()!! } else null

		operator fun next() = when {
			equals(NONE) -> WOODEN
			equals(WOODEN) -> STONE
			else -> null
		}

		override fun equals(other: Any?) = other is Chestplate && tier == other.tier

		companion object {
			var NONE = Chestplate(ArmorTier.NONE, 0, 0)
			var WOODEN = Chestplate(ArmorTier.WOODEN, 100, 100)
			var STONE = Chestplate(ArmorTier.STONE, 250, 250)
		}
	}

	class Leggings(tier: ArmorTier, var damage: Int, var maxDamage: Int): Armories(ArmorType.LEGGINGS, tier) {
		val ints: IntArrayTag
			get() = IntArrayTag("stats", intArrayOf(tier.protection, damage, maxDamage))

		val craftingRecipe: Recipe?
			get() = if (next() != null) ArmorType.LEGGINGS.getRecipeFor(next()!!.tier) { mi -> mi.armor.leggings = next()!! } else null

		operator fun next() = when {
			equals(NONE) -> WOODEN
			equals(WOODEN) -> STONE
			else -> null
		}

		override fun equals(other: Any?) = other is Leggings && tier == other.tier

		companion object {
			var NONE = Leggings(ArmorTier.NONE, 0, 0)
			var WOODEN = Leggings(ArmorTier.WOODEN, 100, 100)
			var STONE = Leggings(ArmorTier.STONE, 250, 250)
		}
	}

	class Boots(tier: ArmorTier, var damage: Int, var maxDamage: Int): Armories(ArmorType.BOOTS, tier) {
		val ints: IntArrayTag
			get() = IntArrayTag("stats", intArrayOf(tier.protection, damage, maxDamage))

		val craftingRecipe: Recipe?
			get() = if (next() != null) ArmorType.BOOTS.getRecipeFor(next()!!.tier) { mi -> mi.armor.boots = next()!! } else null

		operator fun next() = when {
			equals(NONE) -> WOODEN
			equals(WOODEN) -> STONE
			else -> null
		}

		override fun equals(other: Any?) = other is Boots && tier == other.tier

		companion object {
			var NONE = Boots(ArmorTier.NONE, 0, 0)
			var WOODEN = Boots(ArmorTier.WOODEN, 100, 100)
			var STONE = Boots(ArmorTier.STONE, 250, 250)
		}
	}
}