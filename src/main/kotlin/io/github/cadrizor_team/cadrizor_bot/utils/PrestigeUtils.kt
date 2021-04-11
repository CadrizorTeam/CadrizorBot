package io.github.cadrizor_team.cadrizor_bot.utils

import io.github.cadrizor_team.cadrizor_bot.roleplay.SmeltingLevel
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import kotlin.math.roundToInt

object PrestigeUtils {
	fun getFurnaceSize(level: SmeltingLevel, inventory: MemberInventory): Int {
		var size = level.maxInput
		for (i in 0 until inventory.prestige.lvlFurnaceSize) size *= 2
		return size
	}

	fun getDurability(durability: Int, inventory: MemberInventory): Int {
		val percent = (0.05 * inventory.prestige.lvlDurability).toFloat()
		return (durability + durability * percent).roundToInt()
	}
}