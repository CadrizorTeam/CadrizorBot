@file:JvmName("DispatcherUtils")

package io.github.cadrizor_team.cadrizor_bot.utils

import com.mojang.brigadier.context.CommandContext
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import net.dv8tion.jda.api.entities.Member

fun CommandContext<GMREvent>.getMember(argument: String): Member? {
	// Username  : <@526514057512091659>
	// Nickname* : <@!526514057512091659>
	// (*) Per Guild Nickname
	val mention = getArgument(argument, String::class.java).trim()
	val id = mention.substring(if ("!" in mention) 3 else 2, mention.length - 1)
	return source.guild.getMemberById(id)
}