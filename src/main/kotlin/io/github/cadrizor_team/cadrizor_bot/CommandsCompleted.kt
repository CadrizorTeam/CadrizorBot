package io.github.cadrizor_team.cadrizor_bot

import com.mojang.brigadier.context.CommandContext
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent

object CommandsCompleted {
	fun onAdminComplete(context: CommandContext<GMREvent>, success: Boolean, result: Int) {
		println("[Command] ${Main.prefixes.admin}${context.input} execution ${if (success) "success" else "failed"} with code $result !")
	}

	fun onComplete(context: CommandContext<GMREvent>, success: Boolean, result: Int) {
		println("[Command] ${Main.prefixes.user}${context.input} execution ${if (success) "success" else "failed"} with code $result !")
	}
}