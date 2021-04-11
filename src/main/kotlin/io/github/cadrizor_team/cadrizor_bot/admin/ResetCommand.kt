package io.github.cadrizor_team.cadrizor_bot.admin

import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberPrestigeInventory
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.sendVolatileMessage
import io.github.cadrizor_team.cadrizor_bot.utils.Utils.waitXSeconds
import io.github.cadrizor_team.cadrizor_bot.utils.getMember
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

object ResetCommand {
	@JvmStatic
	fun reset() = literal<GMREvent>("reset")
			.requires(GMREvent::isOwner)
			.then(member())
			.then(guild())
			.then(all())!!

	private fun member() = literal<GMREvent>("member")
			.then(memberThisGuild())
			.then(memberAllGuilds())!!

	private fun memberThisGuild() = literal<GMREvent>("this")
			.then(argument<GMREvent, String>("other", string()).executes {
				val event = it.source
				val member = it.getMember("other") ?: return@executes -1

				Main.PROCESS_COMMANDS = false
				val inv = MemberInventory()
				inv.prestige = MemberPrestigeInventory()
				inv.storeData(member)
				sendVolatileMessage(event, ":white_check_mark: Reseted <@!${member.user.id}> !")
				waitXSeconds(6)
				Main.PROCESS_COMMANDS = true
				1
			})!!

	private fun memberAllGuilds() = literal<GMREvent>("all")
			.then(argument<GMREvent, String>("other", word())
					.executes {
						Main.PROCESS_COMMANDS = false
						Main.shardManager.shards.forEach { shard ->
							val user = shard.getUserById(getString(it, "other"))
							shard.guilds.forEach { guild ->
								if (user != null && guild.isMember(user)) {
									val member = guild.getMember(user)
									if (member != null) {
										val inv = MemberInventory()
										inv.prestige = MemberPrestigeInventory()
										inv.storeData(member)
									}
								}
							}
						}
						waitXSeconds(6)
						Main.PROCESS_COMMANDS = true
						1
					})!!

	private fun guild() = literal<GMREvent>("guild")
			.executes {
				Main.PROCESS_COMMANDS = false
				val event = it.source
				with(File("data/g${event.guild.id}.dat")) {
					if (exists())
						try {
							FileUtils.forceDelete(this)
						} catch (e: IOException) {
							e.printStackTrace()
						}
				}
				waitXSeconds(6)
				Main.PROCESS_COMMANDS = true
				1
			}

	private fun all() = literal<GMREvent>("all")
			.executes {
				Main.PROCESS_COMMANDS = false
				try {
					FileUtils.cleanDirectory(File("data"))
				} catch (e: IOException) {
					e.printStackTrace()
				}
				waitXSeconds(6)
				Main.PROCESS_COMMANDS = true
				1
			}!!
}