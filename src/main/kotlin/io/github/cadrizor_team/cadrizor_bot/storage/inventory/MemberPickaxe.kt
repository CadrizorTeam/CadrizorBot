package io.github.cadrizor_team.cadrizor_bot.storage.inventory

import com.flowpowered.nbt.CompoundTag
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.lang.contains
import io.github.cadrizor_team.cadrizor_bot.lang.getEnchants
import io.github.cadrizor_team.cadrizor_bot.lang.get
import io.github.cadrizor_team.cadrizor_bot.lang.set
import io.github.cadrizor_team.cadrizor_bot.lang.putEnchants
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils

class MemberPickaxe(private val index: Int) {
	lateinit var name: String
	var miningLevel = HarvestLevel.HAND
	var durability = miningLevel.durability
	val enchants = HashMap<Enchantment, Int>()

	val hasSmelting get() = EnchantmentsInit.SMELTING in enchants
	val isBroken get() = durability == 0
	val isSilk get() = EnchantmentsInit.SILK_TOUCH in enchants
	val fortuneLevel get() = enchants[EnchantmentsInit.FORTUNE] ?: 0

	constructor(index: Int, miningLevel: HarvestLevel, durability: Int) : this(index) {
		this.miningLevel = miningLevel
		this.durability = durability
	}

	@JvmName("getPickName")
	fun getName(): String = try { name } catch (except: UninitializedPropertyAccessException) { "Pickaxe $index" }

	@JvmOverloads
	fun upgrade(inventory: MemberInventory, miningLevel: HarvestLevel, repair: Boolean = true) {
		this.miningLevel = miningLevel
		this.durability = if (repair) PrestigeUtils.getDurability(miningLevel.durability, inventory) else durability
	}

	fun serialize(pickaxeTag: CompoundTag) {
		pickaxeTag["name"] = getName()
		pickaxeTag["mining_level"] = miningLevel.name
		pickaxeTag["durability"] = durability
		if (enchants.isNotEmpty())
			pickaxeTag.putEnchants("enchants", enchants)
	}

	companion object {
		@JvmStatic
		fun deserialize(index: Int, pickaxeTag: CompoundTag) = MemberPickaxe(index).apply {
			name = pickaxeTag["name" to "Pickaxe $index"]
			miningLevel = HarvestLevel.valueOf(pickaxeTag["mining_level" to "HAND"])
			durability = pickaxeTag["durability" to miningLevel.durability]
			if ("enchants" in pickaxeTag) enchants.putAll(pickaxeTag.getEnchants("enchants"))
		}
	}
}