package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.SwordLevel
import io.github.cadrizor_team.cadrizor_bot.storage.DataStorage
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.PrestigeUtils.getDurability
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage

object SwordInfoCommand {
	@JvmStatic
	fun swordInfo() = literal<GMREvent>("swInf")
			.executes {
				val event = it.source
				val member = event.member!!
				val result = DataStorage.retrieveData(event.guild, member)
				if (result.isCreated)
					sendVolatileMessage(event, "You don't have IG account. We created one for you.")

				val inventory = MemberInventory.deserialize(result.memberTag)
				val message = StringBuilder(":information_source: Sword levels :")
				for (level in SwordLevel.values()) {
					message.append("\n${level.name} [*$level*] Durability: ${level.durability}")
					if (inventory.prestige.nbPrestige > 0 && level.durability > 0)
						message.append(" - With Prestige: ${getDurability(level.durability, inventory)}")
					message.append(" Damage: ${level.damage}")
				}
				sendVolatileMessage(event, "$message")
				1
			}!!
}