package io.github.cadrizor_team.cadrizor_bot.roleplay.mobs

import io.github.cadrizor_team.cadrizor_bot.roleplay.SwordLevel
import net.dv8tion.jda.api.EmbedBuilder
import kotlin.random.Random

object Mobs {
	val MOBS = HashMap<String, Mob>()

	fun init() {
		Mob("zombie", "Zombie", SwordLevel.WOOD, {
			it.xp.addXp(when (Random.nextInt(100)) {
				in 75..99 -> 10
				in 50..74 -> 7
				in 25..49 -> 5
				else -> 2
			})
		}, null)
				.register()

		Mob("skeleton", "Skeleton", SwordLevel.STONE, {
			it.xp.addXp(when (Random.nextInt(100)) {
				in 75..99 -> 20
				in 50..74 -> 15
				in 25..49 -> 10
				else -> 5
			})
		}, null)
				.register()

		Mob("wither", "Wither Skeleton", SwordLevel.IRON, { it.witherSkulls++ }, null)
				.setWinEmbed(EmbedBuilder().addField("Wither Skeleton Skull", "+1", false))
				.register()
	}
}