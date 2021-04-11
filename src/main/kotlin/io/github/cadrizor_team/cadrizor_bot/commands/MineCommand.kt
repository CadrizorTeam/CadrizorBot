package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.roleplay.HarvestLevel
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses.bonusXP
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import kotlin.random.Random

object MineCommand {
	@JvmStatic
	fun mine() = literal<GMREvent>("mine")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, ":warning: You don't have IG account. We created one for you. Please retry after 15s.")
					return@executes -1
				}
				val inv = deserialize(result.memberTag)
				val memberPickaxe = inv.pickaxes[inv.pickaxeIndex]
				val pickaxeLevel = memberPickaxe.miningLevel
				if (pickaxeLevel == HarvestLevel.HAND) {
					CadrizEmbedBuilder.invMissingItem("Pickaxe").send(event)
					return@executes -1
				}
				if (memberPickaxe.durability == 0) {
					CadrizEmbedBuilder.toolBroken("Pickaxe")
							.appendDescription("\nPlease craft another one !")
							.send(event)
					return@executes -1
				}
				val beforeCommand = inv.xp.level
				val mapWin = applyMining(inv)
				val minage = CadrizEmbedBuilder.result("Mining", member)
				var totalMined = mapWin.map.remove("TotalMined")!!
				mapWin.map.forEach { (rs, am) -> minage.addField(rs, am.repr(), true) }
				if (totalMined.get() < 0) totalMined = from(0)
				val xpWon = (Random.nextInt(totalMined.get() / 3 + 1) * bonusXP).toInt()
				if (memberPickaxe.enchants.containsKey(EnchantmentsInit.MENDING)) {
					EnchantmentsInit.MENDING.applyEnchant(inv, Enchantment.EnchantablePart.PICKAXE, 1, xpWon)
					minage.addField("Current XP", "${inv.xp.display()}\n*You have the*\n*MENDING enchantment*", false)
				} else {
					inv.xp.addXp(xpWon)
					minage.addField("XP Collected", "${from(xpWon)}", false)
				}

				when (memberPickaxe.durability) {
					-1 -> minage.addField("Pickaxe", "Unbreakable (${HarvestLevel.CADRIZ.name})", false)
					0 -> minage.addField("Pickaxe", "Broken", false)
					else -> minage.addField("Pickaxe", progress(memberPickaxe.durability, getDurability(pickaxeLevel.durability, inv)), false)
				}

				inv.storeData(member)
				if (inv.xp.level > beforeCommand)
					sendVolatileMessage(event, "You leveled up! New level: ${inv.xp.level}")
				minage.send(event)
				1
			}!!
}