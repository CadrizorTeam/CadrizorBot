package io.github.cadrizor_team.cadrizor_bot.storage

import com.flowpowered.nbt.CompoundTag
import io.github.cadrizor_team.cadrizor_bot.lang.get
import io.github.cadrizor_team.cadrizor_bot.lang.set

object Bonuses {
	internal const val NO_BONUSES = 1.0f

	var bonusWoodcutter = NO_BONUSES
	var bonusMiner = NO_BONUSES
	var bonusXP = NO_BONUSES

	val allowsOverstorage get() = bonusWoodcutter != NO_BONUSES || bonusMiner != NO_BONUSES
	val bypassMiningLimit get() = bonusMiner != NO_BONUSES

	fun deserialize(bonusesCompoundTag: CompoundTag) {
		bonusWoodcutter = bonusesCompoundTag["bonus_woodcutter" to bonusWoodcutter]
		bonusMiner = bonusesCompoundTag["bonus_miner" to bonusMiner]
		bonusXP = bonusesCompoundTag["bonus_XP" to bonusXP]
	}

	fun serialize(bonusesCompoundTag: CompoundTag) {
		bonusesCompoundTag["bonus_woodcutter"] = bonusWoodcutter
		bonusesCompoundTag["bonus_miner"] = bonusMiner
		bonusesCompoundTag["bonus_XP"] = bonusXP
	}
}