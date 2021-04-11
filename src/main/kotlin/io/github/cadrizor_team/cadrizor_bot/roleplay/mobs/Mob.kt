package io.github.cadrizor_team.cadrizor_bot.roleplay.mobs

import io.github.cadrizor_team.cadrizor_bot.roleplay.SwordLevel
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.Random

class Mob(
		val id: String,
		val name: String,
		val minLevel: SwordLevel,
		val winConsumer: ((MemberInventory) -> Unit)?,
		val loseConsumer: ((MemberInventory) -> Unit)?
) {
	private var winEmbed = EmbedBuilder()
	private var loseEmbed = EmbedBuilder()

	fun register() {
		Mobs.MOBS[id] = this
	}

	fun setWinEmbed(builder: EmbedBuilder): Mob {
		this.winEmbed = builder
		return this
	}

	private fun getWinEmbed(member: Member) = CadrizEmbedBuilder.from(winEmbed)
			.setTitle("Mob Defeated!")
			.setDescription("Member ${member.user.id} | <@!${member.user.id}>\nMob: $name") as CadrizEmbedBuilder

	fun setLoseEmbed(builder: EmbedBuilder): Mob {
		this.loseEmbed = builder
		return this
	}

	private fun getLoseEmbed(member: Member) = CadrizEmbedBuilder.from(loseEmbed)
			.setTitle("You Lose!")
			.setDescription("Member ${member.user.id} | <@!${member.user.id}>\nMob: $name") as CadrizEmbedBuilder

	fun fight(event: GuildMessageReceivedEvent, member: Member, inventory: MemberInventory) {
		val yourSword = inventory.swordLevel
		val minSword = minLevel

		val win = yourSword.level() >= minSword.level() && Random().nextInt(100) > 29
		(if (win) winConsumer else loseConsumer)?.invoke(inventory)
		if (inventory.swordDurability != -1) inventory.swordDurability--

		inventory.storeData(member)

		(if (win) getWinEmbed(member) else getLoseEmbed(member)).send(event)
	}
}