package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage.retrieveData
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory.Companion.deserialize
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.progress
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import java.util.*
import kotlin.math.roundToInt

object WitherCommand {
	@JvmStatic
	fun wither() = literal<GMREvent>("wither")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = retrieveData(event.guild, member)
				if (result.isCreated) {
					sendVolatileMessage(event, "You don't have IG account. We created one fot you. Please retry after 15s.")
					return@executes -1
				}
				val inventory = deserialize(result.memberTag)
				val armors = inventory.armor
				if (inventory.witherSkulls < 3) {
					CadrizEmbedBuilder.invMissingItem("Wither Skulls", 3)
							.addField("Progress", progress(inventory.witherSkulls, 3), true)
							.send(event)
					return@executes -1
				}

				inventory.witherSkulls -= 3

				var bossHP = 100.0f
				val damageHP = 17.5f

				var playerHP = 20.0f
				val playerAttackHP = inventory.swordLevel.damage

				var step = 0

				while (bossHP > 0.0f && playerHP > 0.0f) {
					step++

					// Player's Turn
					var parade = Random().nextInt(100)
					var attack = (playerAttackHP / if (parade < 75) 1.0f else 3.0f)
					bossHP -= attack
					sendVolatileMessage(event, "[$step] [Player's Turn] Deal $attack damage. Wither has now $bossHP HP")

					if (bossHP <= 0.0f) break

					// Wither's Turn
					val playerArmor = (armors.helmet.tier.protection + armors.chestplate.tier.protection + armors.leggings.tier.protection + armors.boots.tier.protection).toFloat()
					attack = damageHP - playerArmor / 2
					parade = Random().nextInt(100)
					attack *= if (parade < 75) 1.0f else 0.6f
					playerHP -= attack
					sendVolatileMessage(event, "[$step] [Wither's Turn] Deal $attack damage. You have now $playerHP HP")
				}

				val wither1 = CadrizEmbedBuilder
						.result(if (bossHP <= 0.0f) "Wither / Defeated !" else "Wither / You Lose", member)
						.addField("Steps", "`$step`", true)
				if (bossHP <= 0.0f) {
					wither1.addField("Nether Stars", "`1`", true)
					inventory.netherStar++
				} else wither1.addField("Boss HP", progress(bossHP.roundToInt(), 100), true)
				wither1.send(event)

				inventory.storeData(member)
				1
			}!!
}