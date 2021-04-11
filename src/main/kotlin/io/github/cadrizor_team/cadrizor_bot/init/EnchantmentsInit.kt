package io.github.cadrizor_team.cadrizor_bot.init

import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.*
import kotlin.streams.toList

object EnchantmentsInit {
	val ENCHANTMENTS = ArrayList<Enchantment>()
	val ENCHANTMENTS_DESCRIPTION = HashMap<Enchantment, String>()

	val FORTUNE = EnchantmentFortune()
	val MENDING = EnchantmentMending()
	val SILK_TOUCH = EnchantmentSilkTouch()
	val SMELTING = EnchantmentSmelting()
	val UNBREAKING = EnchantmentUnbreaking()

	fun init() {
		ENCHANTMENTS += FORTUNE
		ENCHANTMENTS += MENDING
		ENCHANTMENTS += SILK_TOUCH
		ENCHANTMENTS += SMELTING
		ENCHANTMENTS += UNBREAKING

		ENCHANTMENTS_DESCRIPTION[FORTUNE] = "Win more gems within mine command"
		ENCHANTMENTS_DESCRIPTION[MENDING] = "Use your xp to repair stuff"
		ENCHANTMENTS_DESCRIPTION[SILK_TOUCH] = "Drops the mined block itself"
		ENCHANTMENTS_DESCRIPTION[SMELTING] = "Automaticaly smelts blocks when broken"
		ENCHANTMENTS_DESCRIPTION[UNBREAKING] = "Your stuff breaks slower"
	}

	operator fun get(name: String): Enchantment? {
		val collected = ENCHANTMENTS.stream().filter { it.id == name.toLowerCase() }.toList()
		return if (collected.isNotEmpty()) collected[0] else null
	}

	operator fun contains(name: String) = this[name] != null
}