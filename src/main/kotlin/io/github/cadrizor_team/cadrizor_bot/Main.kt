package io.github.cadrizor_team.cadrizor_bot

import com.mojang.brigadier.CommandDispatcher
import io.github.cadrizor_team.cadrizor_bot.handling.reactions.MessageReactionHandler
import io.github.cadrizor_team.cadrizor_bot.handling.events.GuildMessageReactionAdd
import io.github.cadrizor_team.cadrizor_bot.handling.events.GuildMessageReceived
import io.github.cadrizor_team.cadrizor_bot.handling.events.PrivateMessageReceived
import io.github.cadrizor_team.cadrizor_bot.handling.events.Ready
import io.github.cadrizor_team.cadrizor_bot.init.CommandsInit
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.init.GlobalShopInit
import io.github.cadrizor_team.cadrizor_bot.init.crafting.CraftingManagerInit
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.roleplay.mobs.Mobs
import io.github.cadrizor_team.cadrizor_bot.storage.BonusesStorage
import io.github.cadrizor_team.cadrizor_bot.utils.ActivityUtils
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS
import net.dv8tion.jda.api.sharding.DefaultShardManager
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import javax.security.auth.login.LoginException

object Main {
	const val VERSION = "0.5.3"
	const val PROJECT_URL = "https://cadrizorteam.cf/bot"
	val ADMIN_DISPATCHER = CommandDispatcher<GMREvent>()
	val USER_DISPATCHER = CommandDispatcher<GMREvent>()
	val GUILDS_LISTENER = GuildListenerThread()
	val IGNORED = ArrayList<String>()
	val MESSAGES_HANDLERS = HashMap<String, MessageReactionHandler>()
	lateinit var shardManager: DefaultShardManager
	lateinit var TC_Cadrizor_admins: TextChannel
	lateinit var prefixes: IPrefixes
	var PROCESS_COMMANDS = true
		set(value) {
			field = value
			ActivityUtils.refreshPresence()
		}
	var dev = false

	@JvmStatic
	@Throws(LoginException::class)
	fun main(args: Array<String>) {
		CadrizChecker.checkArgs(args)

		ADMIN_DISPATCHER.setConsumer(CommandsCompleted::onAdminComplete)
		USER_DISPATCHER.setConsumer(CommandsCompleted::onComplete)

		shardManager = DefaultShardManagerBuilder.createLight(args[0])
				.enableIntents(GUILD_MEMBERS)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableCache(CacheFlag.EMOTE)
				.addEventListeners(Adapter())
				.build() as DefaultShardManager

		BonusesStorage.retrieveBonuses()
		CraftingManagerInit.init()
		GlobalShopInit.init()
		EnchantmentsInit.init()
		Mobs.init()

		CommandsInit.init()
	}

	class Adapter: ListenerAdapter() {
		override fun onReady(event: ReadyEvent) = Ready.listen(event)

		override fun onGuildMessageReceived(event: GMREvent) = GuildMessageReceived.listen(event)

		override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) = GuildMessageReactionAdd.listen(event)

		override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) = PrivateMessageReceived.listen(event)

		override fun onShutdown(event: ShutdownEvent) {
			if (!GUILDS_LISTENER.isInterrupted) GUILDS_LISTENER.interrupt()
		}
	}
}