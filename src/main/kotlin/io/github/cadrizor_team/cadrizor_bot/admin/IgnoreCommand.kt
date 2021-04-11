package io.github.cadrizor_team.cadrizor_bot.admin

import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import io.github.cadrizor_team.cadrizor_bot.utils.getMember

object IgnoreCommand {
	@JvmStatic
	fun ignore() = literal<GMREvent>("ignore")
			.then(literal<GMREvent>("add")
					.then(argument<GMREvent, String>("add_other", word())
							.executes {
								val event = it.source
								val member = it.getMember("add_other") ?: return@executes -1
								Main.IGNORED += "${member.guild.id}|${member.user.id}"
								sendVolatileMessage(event, ":white_check_mark: Ignored member ${member.effectiveName}")
								1
							}))
			.then(literal<GMREvent>("remove")
					.then(argument<GMREvent, String>("remove_other", word())
							.executes {
								val event = it.source
								val member = it.getMember("remove_other") ?: return@executes -1
								if (Main.IGNORED.remove("${member.guild.id}|${member.user.id}"))
									sendVolatileMessage(event, ":white_check_mark: Un-Ignored member ${member.effectiveName}")
								else sendVolatileMessage(event, ":x: Member ${member.effectiveName} is not ignored !")
								1
							}))
			.requires(GMREvent::isOwner)!!
}