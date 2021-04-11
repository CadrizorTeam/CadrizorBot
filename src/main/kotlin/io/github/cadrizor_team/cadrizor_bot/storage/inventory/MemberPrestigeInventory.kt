package io.github.cadrizor_team.cadrizor_bot.storage.inventory

import com.flowpowered.nbt.CompoundMap
import com.flowpowered.nbt.CompoundTag
import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.lang.get
import io.github.cadrizor_team.cadrizor_bot.lang.plusAssign
import io.github.cadrizor_team.cadrizor_bot.lang.set
import kotlin.math.max

class MemberPrestigeInventory {
	var prestige = NumberFactory().get()
	var lastPrestige = NumberFactory().get()
	var nbPrestige = NumberFactory().get()

	var lvlStorage = NumberFactory().get()
	var lvlDurability = NumberFactory().get()
	var lvlFortune = NumberFactory().get()
	var lvlFurnaceSize = NumberFactory().get()
	var lvlSwordDamage = NumberFactory().get()
	var lvlArmorProtection = NumberFactory().get()
	var lvlMoreXp = NumberFactory().get()
	var lvlWoodcutting = NumberFactory().get()
	var lvlWoodcuttingDur = NumberFactory().get()
	var lvlMining = NumberFactory().get()
	var lvlMiningDur = NumberFactory().get()

	val minPrestige: Int get() = max(BASE_PRESTIGE_REQUIRED, lastPrestige)

	fun serialize(memberTagCompound: CompoundTag) {
		val memberPrestigeTag = CompoundTag("prestige", CompoundMap())

		memberPrestigeTag["prestige"] = prestige
		memberPrestigeTag["last_prestige"] = lastPrestige
		memberPrestigeTag["nb_prestige"] = nbPrestige

		memberPrestigeTag["lvl_storage"] = lvlStorage
		memberPrestigeTag["lvl_durability"] = lvlDurability
		memberPrestigeTag["lvl_furnace_size"] = lvlFurnaceSize

		memberTagCompound.value += memberPrestigeTag
	}

	companion object {
		const val BASE_PRESTIGE_REQUIRED = 8

		@JvmStatic
		internal fun deserialize(memberCompoundTag: CompoundTag): MemberInventory {
			val prestigeTag = memberCompoundTag.value["prestige"] as CompoundTag

			val prestige = MemberPrestigeInventory().apply {
				prestige = prestigeTag["prestige" to prestige]
				lastPrestige = prestigeTag["last_prestige" to lastPrestige]
				nbPrestige = prestigeTag["nb_prestige" to nbPrestige]

				lvlStorage = prestigeTag["lvl_storage" to lvlStorage]
				lvlDurability = prestigeTag["lvl_durability" to lvlDurability]
				lvlFurnaceSize = prestigeTag["lvl_furnace_size" to lvlFurnaceSize]
			}

			return MemberInventory().apply { this.prestige = prestige }
		}
	}
}
