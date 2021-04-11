package io.github.cadrizor_team.cadrizor_bot

import io.github.cadrizor_team.cadrizor_bot.lang.commandsCadrizorChannel
import net.dv8tion.jda.api.Permission
import javax.security.auth.login.LoginException

object CadrizChecker {
	private val BASE_PERMS = arrayListOf(
			Permission.MESSAGE_READ,
			Permission.MESSAGE_WRITE,
			Permission.MESSAGE_ADD_REACTION,
			Permission.MESSAGE_EXT_EMOJI,
			Permission.MESSAGE_HISTORY,
			Permission.MESSAGE_MANAGE
	)
	private val NONOP_PERMS = arrayListOf(*Permission.values())
			.apply { this -= Permission.ADMINISTRATOR }

	@Throws(LoginException::class)
	fun checkArgs(args: Array<String>) {
		if (args.isEmpty() || (args.size == 1 && args[0] == "--dev")) throw LoginException("Token not found. Can't login.")
		if (args.size >= 2 && args[1] == "--dev") {
			Main.dev = true
			Main.prefixes = DevelopmentPrefixes()
			return
		}
		Main.prefixes = NormalPrefixes()
	}

	fun checkPerms() {
		for (shard in Main.shardManager.shards) {
			for (guild in shard.guilds) {
				val selfMember = guild.selfMember
				val basePerms = selfMember.hasPermission(BASE_PERMS)
				val allPerms = selfMember.hasPermission(NONOP_PERMS)
				val isAdmin = selfMember.hasPermission(Permission.ADMINISTRATOR)
				val channel = guild.commandsCadrizorChannel != null
				println("${guild.name} [${guild.id}L]:\n" +
						"\t- Base Permissions: ${if (!basePerms) "NOT " else ""}OKAY\n" +
						"\t- All NonOp Permissions: ${if (!allPerms) "NOT " else ""}OKAY${if (isAdmin) " (ADMIN)" else ""}\n" +
						"\t- Specific Channel: ${if (!channel) "NOT " else ""}OKAY")
			}
		}
	}
}