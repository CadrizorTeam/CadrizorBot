package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.armory.Armories
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object InvCommand {
	@JvmStatic
	fun inv() = literal<GMREvent>("inv")
			.then(armor())
			.then(ench())
			.executes { it ->
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) sendVolatileMessage(event, "You don't have IG account. We created one for you.")
				val minv = MemberInventory.deserialize(result.memberTag)
				val inventory = CadrizEmbedBuilder.result("Inventory", member)
						.appendDescription((if (minv.workbench) "\n<:Crafting:740187948561203251> " else "\n:x: ") + "Workbench")
						.appendDescription((if (minv.furnaceLevel.name != "NONE") " | :white_check_mark:" else " | :x:") + " `Furnace: " + minv.furnaceLevel.name + "`")
						.appendDescription("\nPickaxes `c%picks` | Sword `c%sword` | Armor: `c%inv armor`")
				if (minv.enchantbench) inventory.appendDescription(" | Enchants: `c%inv ench`")
				when (minv.axeDurability) {
					Integer.MIN_VALUE -> inventory.addField("Axe", "HAND", true)
					-1 -> inventory.addField("Axe", "Unbreakable (${HarvestLevel.CADRIZ.name})", true)
					0 -> inventory.addField("Axe", "Broken (${minv.axeHarvLvl.name})", true)
					else -> inventory.addField("Axe", progress(minv.axeDurability, getDurability(minv.axeHarvLvl.durability, minv)) + " (" + minv.axeHarvLvl.name + ")", true)
				}
				inventory
						.addField(
								"Basic Resources",
								"Wood ${progress(minv.wood, minv.maxWood)}\n" +
										"Cobble ${progress(minv.cobble, minv.maxCobble)}\n" +
										"Stone ${progress(minv.stone, minv.maxStone)}",
								true)
						.addField(
								"Fuels",
								"Charcoal ${progress(minv.charcoal, minv.maxCharcoal)}\n" +
										"Coal ${progress(minv.coal, minv.maxCoal)}",
								true
						)
						.addField(
								"Ores",
								"Iron ${progress(minv.ironOre, minv.maxIronOre)}\n" +
										"Gold ${progress(minv.goldOre, minv.maxGoldOre)}\n" +
										"Diamond ${progress(minv.diamondOre, minv.maxDiamondOre)}\n" +
										"Emerald ${progress(minv.emeraldOre, minv.maxEmeraldOre)}",
								true
						)
						.addField(
								"Ingots | Gems",
								"Iron ${progress(minv.ironIngot, minv.maxIronIngot)}\n" +
										"Gold ${progress(minv.goldIngot, minv.maxGoldIngot)}\n" +
										"Diamond ${progress(minv.diamond, minv.maxDiamond)}\n" +
										"Emerald ${progress(minv.emerald, minv.maxEmerald)}",
								true
						)
						.addField(
								"Blocks",
								"Iron ${progress(minv.ironBlock, minv.maxIronBlock)}\n" +
										"Gold ${progress(minv.goldBlock, minv.maxGoldBlock)}\n" +
										"Diamond ${progress(minv.diamondBlock, minv.maxDiamondBlock)}\n" +
										"Emerald ${progress(minv.emeraldBlock, minv.maxEmeraldBlock)}",
								true
						)
						.addField(
								"Alloys",
								"Diamerald ${progress(minv.crushedDiamerald, minv.maxCrushedDiamerald)}\n" +
										"**Blocks** ${progress(minv.crushedDiameraldBlock, minv.maxCrushedDiameraldBlock)}",
								true
						)
						.addField(
								"Rare Resources",
								"Obsidian ${progress(minv.obsidian, minv.maxObsidian)}\n" +
										"Wither Skulls ${progress(minv.witherSkulls, minv.maxWitherSkulls)}\n" +
										"Nether Stars ${progress(minv.netherStar, minv.maxNetherStar)}",
								true
						)
						.addField(
								"EndGame Resources",
								"Singularities ${progress(minv.singularity, minv.maxSingularity)}\n" +
										"Ultimate ${progress(minv.ultimate, minv.maxUltimate)}\n" +
										"Cadrizor ${progress(minv.cadrizOre, minv.maxCadrizOre)}\n" +
										"Cadriz ${progress(minv.cadrizIngot, minv.maxCadrizIngot)}",
								true
						)
				if (minv.prestige.nbPrestige > 0)
					inventory.addField("Prestige Points", from(minv.prestige.prestige).repr(), true)
				inventory.send(event)
				1
			}!!

	private fun armor() = literal<GMREvent>("armor")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":x: The first command to execute is `c%inv`.")
					return@executes -1
				}
				val minv = MemberInventory.deserialize(result.memberTag)
				val armori = minv.armor
				val inventory = CadrizEmbedBuilder.result("Inventory / Armor", member)
				if (Armories.Helmet.NONE == armori.helmet)
					inventory.addField("Helmet", "`NONE [0]`", true)
				else inventory.addField("Helmet", "`[${armori.helmet.tier.protection}]` ${progress(armori.helmet.damage, armori.helmet.maxDamage)}", true)
				if (Armories.Chestplate.NONE == armori.chestplate)
					inventory.addField("Chestplate", "`NONE [0]`", true)
				else inventory.addField("Chestplate", "`[${armori.chestplate.tier.protection}]` ${progress(armori.chestplate.damage, armori.chestplate.maxDamage)}", true)
				if (Armories.Leggings.NONE == armori.leggings)
					inventory.addField("Leggings", "`NONE [0]`", true)
				else inventory.addField("Leggings", "`[${armori.leggings.tier.protection}]` ${progress(armori.leggings.damage, armori.leggings.maxDamage)}", true)
				if (Armories.Boots.NONE == armori.boots)
					inventory.addField("Boots", "`NONE [0]`", true)
				else inventory.addField("Boots", "`[${armori.boots.tier.protection}]` ${progress(armori.boots.damage, armori.boots.maxDamage)}", true)
				inventory.addField("Upgrade ?", "- `c%craft armorbench`\n- `c%armorcraft [<part> [upgrade]]`", false)
						.send(event)
				1
			}

	private fun ench() = literal<GMREvent>("ench")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, "You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}
				val minv = MemberInventory.deserialize(result.memberTag)
				if (!minv.enchantbench) {
					CadrizEmbedBuilder
							.missingFactory("Enchantbench", "${Main.prefixes.user}craft enchantbench")
							.send(event)
					return@executes -1
				}
				CadrizEmbedBuilder.result("Inventory / Enchantment", member)
						.addField("Pickaxe", writeEnchants(minv.pickaxe.enchants), false)
						.addField("Axe", writeEnchants(minv.axeEnchants), false)
						.addField("Sword", writeEnchants(minv.swordEnchants), false)
						.send(event)
				1
			}

	private fun writeEnchants(enchants: HashMap<Enchantment, Int>): String {
		val enchantsStr = arrayListOf<String>()
		for (enchant in enchants.entries) {
			val builder = StringBuilder("• ${enchant.key.name}")
			if (enchant.key.maxLevel != 1) builder.append(" ${enchant.value}")
			enchantsStr += "$builder"
		}
		return enchantsStr.joinToString("\n")
				.replace("5".toRegex(), "V")
				.replace("4".toRegex(), "IV")
				.replace("3".toRegex(), "III")
				.replace("2".toRegex(), "II")
				.replace("1".toRegex(), "I")
	}
}