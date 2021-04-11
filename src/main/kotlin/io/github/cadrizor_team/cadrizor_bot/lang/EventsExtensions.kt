@file:JvmName("EventsExtensions")
package io.github.cadrizor_team.cadrizor_bot.lang

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

typealias GMREvent = GuildMessageReceivedEvent
val GMREvent.isOwner get() = author.id == "439490684807020555"