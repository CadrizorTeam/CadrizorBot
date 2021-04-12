package io.github.cadrizor_team.cadrizor_bot.storage.inventory

import com.flowpowered.nbt.CompoundMap
import com.flowpowered.nbt.CompoundTag
import com.flowpowered.nbt.IntArrayTag
import io.github.cadrizor_team.cadrizor_bot.lang.get
import io.github.cadrizor_team.cadrizor_bot.lang.plusAssign
import io.github.cadrizor_team.cadrizor_bot.lang.set
import io.github.cadrizor_team.cadrizor_bot.roleplay.armory.ArmorTier
import io.github.cadrizor_team.cadrizor_bot.roleplay.armory.ArmorType
import io.github.cadrizor_team.cadrizor_bot.roleplay.armory.Armories

class MemberArmorInventory {
	var helmet: Armories.Helmet = Armories.Helmet.NONE
	var chestplate: Armories.Chestplate = Armories.Chestplate.NONE
	var leggings: Armories.Leggings = Armories.Leggings.NONE
	var boots: Armories.Boots = Armories.Boots.NONE

	fun serialize(memberInventoryTag: CompoundTag) {
		val memberArmorTag = CompoundTag("armor", CompoundMap())

		memberArmorTag.value += produceTag(ArmorType.HELMET, helmet)
		memberArmorTag.value += produceTag(ArmorType.CHESTPLATE, chestplate)
		memberArmorTag.value += produceTag(ArmorType.LEGGINGS, leggings)
		memberArmorTag.value += produceTag(ArmorType.BOOTS, boots)

		memberInventoryTag.value += memberArmorTag
	}

	private fun produceArmor(type: ArmorType, armoryTag: CompoundTag): Armories? {
		val tier = ArmorTier.valueOf(armoryTag["tier" to "none"].toUpperCase())
		val stats = (armoryTag.value["stats"] as IntArrayTag).value
		if (type == ArmorType.HELMET) return Armories.Helmet(tier, stats[1], stats[2])
		if (type == ArmorType.CHESTPLATE) return Armories.Chestplate(tier, stats[1], stats[2])
		if (type == ArmorType.LEGGINGS) return Armories.Leggings(tier, stats[1], stats[2])
		return if (type == ArmorType.BOOTS) Armories.Boots(tier, stats[1], stats[2]) else null
	}

	private fun produceTag(type: ArmorType, armory: Armories): CompoundTag {
		val armoryTag = CompoundTag(type.name.toLowerCase(), CompoundMap())
		armoryTag["tier"] = armory.tier.name.toLowerCase()
		when (armory) {
			is Armories.Helmet -> armoryTag["stats"] = armory.ints
			is Armories.Chestplate -> armoryTag["stats"] = armory.ints
			is Armories.Leggings -> armoryTag["stats"] = armory.ints
			is Armories.Boots -> armoryTag["stats"] = armory.ints
		}
		return armoryTag
	}

	companion object {
		@JvmStatic
		internal fun deserialize(memberInventoryTag: CompoundTag): MemberArmorInventory {
			val memberArmorTag = memberInventoryTag.value["armor"] as? CompoundTag ?: return MemberArmorInventory()

			val armor = MemberArmorInventory()
			armor.helmet = armor.produceArmor(ArmorType.HELMET, memberArmorTag.value["helmet"] as CompoundTag) as Armories.Helmet
			armor.chestplate = armor.produceArmor(ArmorType.CHESTPLATE, memberArmorTag.value["chestplate"] as CompoundTag) as Armories.Chestplate
			armor.leggings = armor.produceArmor(ArmorType.LEGGINGS, memberArmorTag.value["leggings"] as CompoundTag) as Armories.Leggings
			armor.boots = armor.produceArmor(ArmorType.BOOTS, memberArmorTag.value["boots"] as CompoundTag) as Armories.Boots

			return armor
		}
	}
}