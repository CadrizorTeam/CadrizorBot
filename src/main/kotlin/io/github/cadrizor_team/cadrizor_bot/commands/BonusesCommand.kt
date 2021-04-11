package io.github.cadrizor_team.cadrizor_bot.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses
import io.github.cadrizor_team.cadrizor_bot.storage.BonusesStorage
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder

object BonusesCommand {
	@JvmStatic
	fun bonuses() = literal<GMREvent>("bonuses")
			.executes { context ->
				BonusesStorage.retrieveBonuses()
				val event = context.source
				val member = event.member!!
				CadrizEmbedBuilder.result("Bonuses", member)
						.addField("WoodCutting Bonus", "${from((Bonuses.bonusWoodcutter * 100).toInt())}%", true)
						.addField("Minage Bonus", "${from((Bonuses.bonusMiner * 100).toInt())}%", true)
						.addField("XP Bonus", "${from((Bonuses.bonusXP * 100).toInt())}%", true)
						.addField("Bypass Mining Limits", if (Bonuses.bypassMiningLimit) "Yes" else "No", true)
						.addField("OverStorage Allowed", if (Bonuses.allowsOverstorage) "Yes" else "No", true)
						.send(event)
				1
			}!!
}