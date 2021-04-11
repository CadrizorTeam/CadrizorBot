package io.github.cadrizor_team.cadrizor_bot.admin

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import kotlin.system.exitProcess

object StopCommand {
	@JvmStatic
	fun stop() = literal<GMREvent>("stop")
			.requires(GMREvent::isOwner)
			.executes {
				try {
					Main.GUILDS_LISTENER.interrupt()
				} catch (interrupt: InterruptedException) {
					println("Interrupted CadrizorListener#guildsListener")
				}
				Main.PROCESS_COMMANDS = false
				Main.shardManager.setStatus(OnlineStatus.INVISIBLE)
				Main.shardManager.shards.forEach(JDA::shutdown)
				println("Bye bye Guys!")
				exitProcess(0)
			}!!
}