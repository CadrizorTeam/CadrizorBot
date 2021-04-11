package io.github.cadrizor_team.cadrizor_bot

import io.github.cadrizor_team.cadrizor_bot.utils.Utils
import io.github.cadrizor_team.cadrizor_bot.utils.ActivityUtils

class GuildListenerThread : Thread({
	while (true) {
		Utils.waitXSeconds(10)
		ActivityUtils.refreshPresence()
	}
})