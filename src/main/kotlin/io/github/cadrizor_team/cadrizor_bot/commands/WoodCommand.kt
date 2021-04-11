package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel.CADRIZOR
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel.HAND
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses.allowsOverstorage
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses.bonusWoodcutter
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses.bonusXP
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import kotlin.math.min
import kotlin.random.Random

object WoodCommand {
	@JvmStatic
	fun wood() = literal<GMREvent>("wood")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}
				val inv = deserialize(result.memberTag)
				val beforeCommand = inv.xp.level
				val axeLevel = inv.axeHarvLvl
				var addwood =
						if (axeLevel == HAND || axeLevel == CADRIZOR || inv.axeDurability > 0) Random.Default.nextInt(11 * (axeLevel.level() + 1))
						else 0
				if (inv.axeDurability == 0) {
					CadrizEmbedBuilder.toolBroken("Axe")
							.appendDescription("\nPlease craft another one !").send(event)
					return@executes -1
				}
				val isHand = inv.axeHarvLvl == HAND
				if (isHand || inv.axeDurability > 0) {
					if (!isHand) {
						if (addwood / 2 >= inv.axeDurability) addwood = inv.axeDurability * 2
						inv.axeDurability -= addwood / 2
						if (inv.axeEnchants.containsKey(EnchantmentsInit.UNBREAKING)) {
							val level = inv.axeEnchants[EnchantmentsInit.UNBREAKING] ?: 0
							EnchantmentsInit.UNBREAKING.applyEnchant(inv, Enchantment.EnchantablePart.AXE, level, addwood)
						}
					}
				}
				val addwoodBonus = (addwood * bonusWoodcutter).toInt()
				val xpWon = (Random.nextInt(addwoodBonus / 3 + 1) * bonusXP).toInt()
				if (allowsOverstorage) inv.wood += addwoodBonus
				else inv.wood = min(inv.wood + addwoodBonus, inv.maxWood)
				val hasMending = inv.axeEnchants.containsKey(EnchantmentsInit.MENDING)
				if (hasMending) EnchantmentsInit.MENDING.applyEnchant(inv, Enchantment.EnchantablePart.AXE, 1, xpWon)
				else inv.xp.addXp(xpWon)
				val afterCommand = inv.xp.level
				val woodWon = CadrizEmbedBuilder.result("Woodcutting", member)
						.addField("Won", "${from(addwood)}", true)
						.addField("You have", progress(inv.wood, inv.maxWood), true)
				if (hasMending)
					woodWon.addField("Current XP", "${inv.xp.display()}\n*You have the*\n*MENDING enchantment*", false)
				else woodWon.addField("XP Collected", "${from(xpWon)}", false)
				when (inv.axeDurability) {
					Integer.MIN_VALUE -> woodWon.addField("Axe", "HAND", true)
					-1 -> woodWon.addField("Axe", "Unbreakable (${HarvestLevel.CADRIZ.name})", false)
					0 -> woodWon.addField("Axe", "Broken", false)
					else -> woodWon.addField("Axe", progress(inv.axeDurability, getDurability(inv.axeHarvLvl.durability, inv)), false)
				}
				inv.storeData(member)
				if (afterCommand > beforeCommand)
					sendVolatileMessage(event, "You leveled up! New level: $afterCommand")
				woodWon.send(event)
				1
			}!!
}