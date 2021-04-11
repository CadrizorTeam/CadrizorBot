package io.github.cadrizor_team.cadrizor_bot.storage.inventory

import com.flowpowered.nbt.*
import io.github.cadrizor_team.cadrizor_bot.NumberFactory
import io.github.cadrizor_team.cadrizor_bot.lang.*
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.SmeltingLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.SwordLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.utils.XPNumber
import net.dv8tion.jda.api.entities.Member

class MemberInventory {
	var prestige = MemberPrestigeInventory()

	var money = NumberFactory.zero().toLong()
	var xp = XPNumber()
		private set
	var claimedLvl = NumberFactory.zero()

	var wood = NumberFactory.zero()
	var maxWood = NumberFactory(k = 20).get()

	var charcoal = NumberFactory.zero()
	var maxCharcoal = NumberFactory(k = 15).get()
	var coal = NumberFactory.zero()
	var maxCoal = NumberFactory(k = 15).get()

	var workbench = false
	var armorbench = false
	var enchantbench = false

	var cobble = NumberFactory.zero()
	var maxCobble = NumberFactory(k = 20).get()

	var furnaceLevel = SmeltingLevel.NONE

	var stone = NumberFactory.zero()
	var maxStone = NumberFactory(k = 20).get()

	var axeDurability = Integer.MIN_VALUE
	var axeHarvLvl = HarvestLevel.HAND
	val axeEnchants = HashMap<Enchantment, Int>()

	var pickaxe: MemberPickaxe
		get() = pickaxes[pickaxeIndex]
		set(value) {
			pickaxes[pickaxeIndex] = value
		}

	var pickaxes = Array(2) { MemberPickaxe(it) }
	var pickaxeIndex = 0
	var pickaxeSlots = 2

	var swordDurability = Integer.MIN_VALUE
	var swordLevel = SwordLevel.HAND
	val swordEnchants = HashMap<Enchantment, Int>()

	var ironOre = NumberFactory.zero()
	var maxIronOre = NumberFactory(k = 20).get()
	var ironIngot = NumberFactory.zero()
	var maxIronIngot = NumberFactory(k = 20).get()
	var ironBlock = NumberFactory.zero()
	var maxIronBlock = NumberFactory(u = 500).get()

	var goldOre = NumberFactory.zero()
	var maxGoldOre = NumberFactory(k = 10).get()
	var goldIngot = NumberFactory.zero()
	var maxGoldIngot = NumberFactory(k = 10).get()
	var goldBlock = NumberFactory.zero()
	var maxGoldBlock = NumberFactory(u = 250).get()

	var diamondOre = NumberFactory.zero()
	var maxDiamondOre = NumberFactory(k = 5).get()
	var diamond = NumberFactory.zero()
	var maxDiamond = NumberFactory(k = 5).get()
	var diamondBlock = NumberFactory.zero()
	var maxDiamondBlock = NumberFactory(u = 125).get()

	var emeraldOre = NumberFactory.zero()
	var maxEmeraldOre = NumberFactory(k = 2).get()
	var emerald = NumberFactory.zero()
	var maxEmerald = NumberFactory(k = 2).get()
	var emeraldBlock = NumberFactory.zero()
	var maxEmeraldBlock = NumberFactory(u = 50).get()

	var crushedDiamerald = NumberFactory.zero()
	var maxCrushedDiamerald = NumberFactory(k = 1).get()
	var crushedDiameraldBlock = NumberFactory.zero()
	var maxCrushedDiameraldBlock = NumberFactory(u = 25).get()

	var obsidian = NumberFactory.zero()
	var maxObsidian = NumberFactory(k = 1).get()

	var witherSkulls = NumberFactory.zero()
	var maxWitherSkulls = NumberFactory(u = 300).get()
	var netherStar = NumberFactory.zero()
	var maxNetherStar = NumberFactory(k = 2).get()

	var singularity = NumberFactory.zero()
	var maxSingularity = NumberFactory(k = 1).get()
	var ultimate = NumberFactory.zero()
	var maxUltimate = NumberFactory(u = 500).get()

	var cadrizOre = NumberFactory.zero()
	var maxCadrizOre = NumberFactory(u = 250).get()
	var cadrizIngot = NumberFactory.zero()
	var maxCadrizIngot = NumberFactory(u = 125).get()

	var armor = MemberArmorInventory()

	init {
		applyStorageUpgrade()
	}

	private fun maxima(it: Int): Int {
		var store = it.toDouble()
		for (i in 1..prestige.lvlStorage) {
			store *= 3.0
			store /= 2.0
		}
		return store.toInt()
	}

	fun applyStorageUpgrade() {
		val e = MemberInventory()
		maxWood = maxima(e.maxWood)
		maxCharcoal = maxima(e.maxCharcoal)
		maxCoal = maxima(e.maxCoal)
		maxCobble = maxima(e.maxCobble)
		maxStone = maxima(e.maxStone)
		maxIronOre = maxima(e.maxIronOre)
		maxGoldOre = maxima(e.maxGoldOre)
		maxDiamondOre = maxima(e.maxDiamondOre)
		maxEmeraldOre = maxima(e.maxEmeraldOre)
		maxIronIngot = maxima(e.maxIronIngot)
		maxGoldIngot = maxima(e.maxGoldIngot)
		maxDiamond = maxima(e.maxDiamond)
		maxEmerald = maxima(e.maxEmerald)
		maxIronBlock = maxima(e.maxIronBlock)
		maxGoldBlock = maxima(e.maxGoldBlock)
		maxDiamondBlock = maxima(e.maxDiamondBlock)
		maxEmeraldBlock = maxima(e.maxEmeraldBlock)
		maxCrushedDiamerald = maxima(e.maxCrushedDiamerald)
		maxCrushedDiameraldBlock = maxima(e.maxCrushedDiameraldBlock)
		maxObsidian = maxima(e.maxObsidian)
		maxWitherSkulls = maxima(e.maxWitherSkulls)
		maxNetherStar = maxima(e.maxNetherStar)
		maxSingularity = maxima(e.maxSingularity)
		maxUltimate = maxima(e.maxUltimate)
		maxCadrizOre = maxima(e.maxCadrizOre)
		maxCadrizIngot = maxima(e.maxCadrizIngot)
	}

	fun storeData(member: Member) {
		CompoundTag("m${member.user.id}", CompoundMap()).apply {
			this@MemberInventory.serialize(this)
			DataStorage.storeData(member, this)
		}
	}

	fun serialize(tagCompound: CompoundTag) {
		prestige.serialize(tagCompound)
		val inventoryTag = CompoundTag("inventory", CompoundMap())

		inventoryTag.value += LongTag("money", money)
		inventoryTag.value += IntTag("xp", xp.rawXP())

		inventoryTag.value += IntTag("wood", wood)
		inventoryTag.value += IntTag("wood_max", maxWood)

		inventoryTag.value += IntTag("charcoal", charcoal)
		inventoryTag.value += IntTag("charcoal_max", maxCharcoal)
		inventoryTag.value += IntTag("coal", coal)
		inventoryTag.value += IntTag("coal_max", maxCoal)

		inventoryTag.value += ByteTag("workbench", workbench)
		inventoryTag.value += ByteTag("armorbench", armorbench)
		inventoryTag.value += ByteTag("enchantbench", enchantbench)

		inventoryTag.value += IntTag("cobble", cobble)
		inventoryTag.value += IntTag("cobble_max", maxCobble)

		inventoryTag.value += StringTag("furnace", furnaceLevel.name)

		inventoryTag.value += IntTag("stone", stone)
		inventoryTag.value += IntTag("stone_max", maxStone)

		inventoryTag.value += IntTag("axe_durability", axeDurability)
		inventoryTag.value += StringTag("axe_harvlvl", axeHarvLvl.name)
		inventoryTag.putEnchants("axe_enchants", axeEnchants)

		val pickaxesTags = ArrayList<CompoundTag>(pickaxes.size)
		for (pickaxe in pickaxes) {
			val pickaxeTag = CompoundTag("pickaxe", CompoundMap())
			pickaxe.serialize(pickaxeTag)
			pickaxesTags += pickaxeTag
		}
		inventoryTag.value += ListTag("pickaxes", CompoundTag::class.java, pickaxesTags)
		inventoryTag.value += IntTag("pickaxeIndex", pickaxeIndex)
		inventoryTag.value += IntTag("pickaxeSlots", pickaxeSlots)

		inventoryTag.value += IntTag("sword_durability", swordDurability)
		inventoryTag.value += StringTag("sword_harvlvl", swordLevel.name)
		inventoryTag.putEnchants("sword_enchants", swordEnchants)

		inventoryTag.value += IntTag("iron_ore", ironOre)
		inventoryTag.value += IntTag("iron_ore_max", maxIronOre)
		inventoryTag.value += IntTag("gold_ore", goldOre)
		inventoryTag.value += IntTag("gold_ore_max", maxGoldOre)
		inventoryTag.value += IntTag("diamond_ore", diamondOre)
		inventoryTag.value += IntTag("diamond_ore_max", maxDiamondOre)
		inventoryTag.value += IntTag("emerald_ore", emeraldOre)
		inventoryTag.value += IntTag("emerald_ore_max", maxEmeraldOre)

		inventoryTag.value += IntTag("iron", ironIngot)
		inventoryTag.value += IntTag("iron_max", maxIronIngot)
		inventoryTag.value += IntTag("gold", goldIngot)
		inventoryTag.value += IntTag("gold_max", maxGoldIngot)
		inventoryTag.value += IntTag("diamond", diamond)
		inventoryTag.value += IntTag("diamond_max", maxDiamond)
		inventoryTag.value += IntTag("emerald", emerald)
		inventoryTag.value += IntTag("emerald_max", maxEmerald)

		inventoryTag["iron_block"] = ironBlock
		inventoryTag["iron_block_max"] = maxIronBlock
		inventoryTag["gold_block"] = goldBlock
		inventoryTag["gold_block_max"] = maxGoldBlock
		inventoryTag["diamond_block"] = diamondBlock
		inventoryTag["diamond_block_max"] = maxDiamondBlock
		inventoryTag["emerald_block"] = emeraldBlock
		inventoryTag["emerald_block_max"] = maxEmeraldBlock

		inventoryTag["crushed_diamerald"] = crushedDiamerald
		inventoryTag["crushed_diamerald_max"] = maxCrushedDiamerald
		inventoryTag["crushed_diamerald_block"] = crushedDiameraldBlock
		inventoryTag["crushed_diamerald_block_max"] = maxCrushedDiameraldBlock

		inventoryTag["obsidian"] = obsidian
		inventoryTag["obsidian_max"] = maxObsidian

		inventoryTag["wither_skull"] = witherSkulls
		inventoryTag["wither_skull_max"] = maxWitherSkulls
		inventoryTag["nether_star"] = netherStar
		inventoryTag["nether_star_max"] = maxNetherStar

		inventoryTag["singularity"] = singularity
		inventoryTag["singularity_max"] = maxSingularity
		inventoryTag["ultimate"] = ultimate
		inventoryTag["ultimate_max"] = maxUltimate

		inventoryTag["cadrizor"] = cadrizOre
		inventoryTag["cadrizor_max"] = maxCadrizOre
		inventoryTag["cadriz"] = cadrizIngot
		inventoryTag["cadriz_max"] = maxCadrizIngot

		armor.serialize(inventoryTag)

		tagCompound.value += inventoryTag
	}

	companion object {
		@JvmStatic
		fun deserialize(compoundTag: CompoundTag): MemberInventory {
			val inventory = MemberPrestigeInventory.deserialize(compoundTag)

			val inventoryTag = compoundTag.value["inventory"] as CompoundTag

			inventory.money = inventoryTag["money" to inventory.money]
			inventory.xp = XPNumber(
					NumberFactory.from(inventoryTag["xp" to inventory.xp.rawXP()])
			)

			inventory.wood = inventoryTag["wood" to inventory.wood]
			inventory.maxWood = inventoryTag["wood_max" to inventory.maxWood]

			inventory.charcoal = inventoryTag["charcoal" to inventory.charcoal]
			inventory.maxCharcoal = inventoryTag["charcoal_max" to inventory.maxCharcoal]
			inventory.coal = inventoryTag["coal" to inventory.coal]
			inventory.maxCoal = inventoryTag["coal_max" to inventory.maxCoal]

			inventory.workbench = inventoryTag["workbench" to false]
			inventory.armorbench = inventoryTag["armorbench" to false]
			inventory.enchantbench = inventoryTag["enchantbench" to false]

			inventory.cobble = inventoryTag["cobble" to inventory.cobble]
			inventory.maxCobble = inventoryTag["cobble_max" to inventory.maxCobble]

			inventory.furnaceLevel = SmeltingLevel.valueOf(inventoryTag["furnace" to "NONE"])

			inventory.stone = inventoryTag["stone" to inventory.stone]
			inventory.maxStone = inventoryTag["stone_max" to inventory.maxStone]

			inventory.axeDurability = inventoryTag["axe_durability" to inventory.axeDurability]
			inventory.axeHarvLvl = HarvestLevel.valueOf(inventoryTag["axe_harvlvl" to "HAND"])
			inventory.axeEnchants.putAll(inventoryTag.getEnchants("axe_enchants"))

			inventory.pickaxeIndex = inventoryTag["pickaxeIndex" to inventory.pickaxeIndex]
			inventory.pickaxeSlots = inventoryTag["pickaxeSlots" to inventory.pickaxeSlots]
			if (inventory.pickaxeIndex >= inventory.pickaxeSlots)
				inventory.pickaxeIndex = inventory.pickaxeSlots - 1
			inventoryTag.value["pickaxes"]?.let { pickaxesITag ->
				if (pickaxesITag is ListTag<*>) {
					val pickaxes = pickaxesITag.value
					inventory.pickaxes = Array(inventory.pickaxeSlots) {
						if (it >= pickaxes.size) return@Array MemberPickaxe(it)
						if (pickaxes[it] is CompoundTag && pickaxes[it].type == TagType.TAG_COMPOUND)
							return@Array MemberPickaxe.deserialize(it, pickaxes[it] as CompoundTag)
						MemberPickaxe(it)
					}
				}
			}

			inventory.swordDurability = inventoryTag["sword_durability" to inventory.swordDurability]
			inventory.swordLevel = SwordLevel.valueOf(inventoryTag["sword_harvlvl" to "HAND"])
			inventory.swordEnchants.putAll(inventoryTag.getEnchants("sword_enchants"))

			inventory.ironOre = inventoryTag["iron_ore" to inventory.ironOre]
			inventory.maxIronOre = inventoryTag["iron_ore_max" to inventory.maxIronOre]
			inventory.goldOre = inventoryTag["gold_ore" to inventory.goldOre]
			inventory.maxGoldOre = inventoryTag["gold_ore_max" to inventory.maxGoldOre]
			inventory.diamondOre = inventoryTag["diamond_ore" to inventory.diamondOre]
			inventory.maxDiamondOre = inventoryTag["diamond_ore_max" to inventory.maxDiamondOre]
			inventory.emeraldOre = inventoryTag["emerald_ore" to inventory.emeraldOre]
			inventory.maxEmeraldOre = inventoryTag["emerald_ore_max" to inventory.maxEmeraldOre]

			inventory.ironIngot = inventoryTag["iron" to inventory.ironIngot]
			inventory.maxIronIngot = inventoryTag["iron_max" to inventory.maxIronIngot]
			inventory.goldIngot = inventoryTag["gold" to inventory.goldIngot]
			inventory.maxGoldIngot = inventoryTag["gold_max" to inventory.maxGoldIngot]
			inventory.diamond = inventoryTag["diamond" to inventory.diamond]
			inventory.maxDiamond = inventoryTag["diamond_max" to inventory.maxDiamond]
			inventory.emerald = inventoryTag["emerald" to inventory.emerald]
			inventory.maxEmerald = inventoryTag["emerald_max" to inventory.maxEmerald]

			inventory.ironBlock = inventoryTag["iron_block" to inventory.ironBlock]
			inventory.maxIronBlock = inventoryTag["iron_block_max" to inventory.maxIronBlock]
			inventory.goldBlock = inventoryTag["gold_block" to inventory.goldBlock]
			inventory.maxGoldBlock = inventoryTag["gold_block_max" to inventory.maxGoldBlock]
			inventory.diamondBlock = inventoryTag["diamond_block" to inventory.diamondBlock]
			inventory.maxDiamondBlock = inventoryTag["diamond_block_max" to inventory.maxDiamondBlock]
			inventory.emeraldBlock = inventoryTag["emerald_block" to inventory.emeraldBlock]
			inventory.maxEmeraldBlock = inventoryTag["emerald_block_max" to inventory.maxEmeraldBlock]

			inventory.crushedDiamerald = inventoryTag["crushed_diamerald" to inventory.crushedDiamerald]
			inventory.maxCrushedDiamerald = inventoryTag["crushed_diamerald_max" to inventory.maxCrushedDiamerald]
			inventory.crushedDiameraldBlock = inventoryTag["crushed_diamerald_block" to inventory.crushedDiameraldBlock]
			inventory.maxCrushedDiameraldBlock = inventoryTag["crushed_diamerald_block_max" to inventory.maxCrushedDiameraldBlock]

			inventory.obsidian = inventoryTag["obsidian" to inventory.obsidian]
			inventory.maxObsidian = inventoryTag["obsidian_max" to inventory.maxObsidian]

			inventory.witherSkulls = inventoryTag["wither_skull" to inventory.witherSkulls]
			inventory.maxWitherSkulls = inventoryTag["wither_skull_max" to inventory.maxWitherSkulls]
			inventory.netherStar = inventoryTag["nether_star" to inventory.netherStar]
			inventory.maxNetherStar = inventoryTag["nether_star_max" to inventory.maxNetherStar]

			inventory.singularity = inventoryTag["singularity" to inventory.singularity]
			inventory.maxSingularity = inventoryTag["singularity_max" to inventory.maxSingularity]
			inventory.ultimate = inventoryTag["ultimate" to inventory.ultimate]
			inventory.maxUltimate = inventoryTag["ultimate_max" to inventory.maxUltimate]

			inventory.cadrizOre = inventoryTag["cadrizor" to inventory.cadrizOre]
			inventory.maxCadrizOre = inventoryTag["cadrizor_max" to inventory.maxCadrizOre]
			inventory.cadrizIngot = inventoryTag["cadriz" to inventory.cadrizIngot]
			inventory.maxCadrizIngot = inventoryTag["cadriz_max" to inventory.maxCadrizIngot]

			inventory.armor = MemberArmorInventory.deserialize(inventoryTag)

			return inventory
		}
	}
}