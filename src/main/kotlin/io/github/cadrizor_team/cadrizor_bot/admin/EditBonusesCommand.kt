package io.github.cadrizor_team.cadrizor_bot.admin

import com.mojang.brigadier.arguments.FloatArgumentType.floatArg
import com.mojang.brigadier.arguments.FloatArgumentType.getFloat
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses
import io.github.cadrizor_team.cadrizor_bot.storage.Bonuses.NO_BONUSES
import io.github.cadrizor_team.cadrizor_bot.storage.BonusesStorage
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import java.time.Instant

object EditBonusesCommand {
	@JvmStatic
	fun editBonuses() = literal<GMREvent>("editbonuses")
			.requires(GMREvent::isOwner)
			.then(argument<GMREvent, String>("name", word())
					.then(setValue())
					.then(resetValue()))!!

	private fun setValue() = literal<GMREvent>("set")
			.then(argument<GMREvent, Float>("value", floatArg(NO_BONUSES))
					.executes {
						BonusesStorage.retrieveBonuses()
						val bonus = getString(it, "name")
						val value = getFloat(it, "value")
						when (bonus) {
							"woodcutter" -> Bonuses.bonusWoodcutter = value
							"miner" -> Bonuses.bonusMiner = value
							"xp" -> Bonuses.bonusXP = value
							else -> {}
						}
						BonusesStorage.storeBonuses()
						Main.shardManager.shards.forEach { jda ->
							jda.guilds.forEach { guild ->
								CadrizEmbedBuilder.create()
										.setTitle("Cadrizor Bot - Bonus Modification")
										.addField("Bonus", bonus, true)
										.addField("Value", "`${value * 100}%`", true)
										.setFooter("Issued by ${it.source.member!!}")
										.setTimestamp(Instant.now())
										.send(guild)
							}
						}
						1
					})!!

	private fun resetValue() = literal<GMREvent>("reset")
			.executes {
				BonusesStorage.retrieveBonuses()
				val bonus = getString(it, "name")
				when (bonus) {
					"woodcutter" -> Bonuses.bonusWoodcutter = NO_BONUSES
					"miner" -> Bonuses.bonusMiner = NO_BONUSES
					"xp" -> Bonuses.bonusXP = NO_BONUSES
					else -> {
					}
				}
				BonusesStorage.storeBonuses()
				Main.shardManager.shards.forEach { jda ->
					jda.guilds.forEach { guild ->
						CadrizEmbedBuilder.create()
								.setTitle("Cadrizor Bot - Bonus Modification")
								.appendDescription("Bonus $bonus was reset to its initial state")
								.addField("Bonus", bonus, true)
								.addField("Value", "`${NO_BONUSES * 100}%`", true)
								.setFooter("Issued by ${it.source.member!!}")
								.setTimestamp(Instant.now())
								.send(guild)
					}
				}
				1
			}!!
}