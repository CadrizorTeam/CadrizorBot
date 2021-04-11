package io.github.cadrizor_team.cadrizor_bot.handling.events

import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

object PrivateMessageReceived : EventHandler<PrivateMessageReceivedEvent> {
	override fun listen(event: PrivateMessageReceivedEvent) {
		if (!event.author.isBot)
			CadrizEmbedBuilder.create()
					.setTitle("Cadrizor Bot [Forbidden]")
					.appendDescription((if (Main.dev) "Development " else "") + "Version ${Main.VERSION}")
					.addField("Links", "[『 Help 』](${Main.PROJECT_URL})\n[『 Issues 』](${Main.PROJECT_URL}-issues/)", true)
					.sendPrivate(event.author)
	}
}