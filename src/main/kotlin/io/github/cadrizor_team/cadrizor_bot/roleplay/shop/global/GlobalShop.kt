package io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global

import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.Shop
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.IEmbedable
import net.dv8tion.jda.api.entities.Member

object GlobalShop : Shop<GlobalShopItem, GlobalShopCategory>(), IEmbedable {
	fun clearCategories() = categories.clear()

	override fun getEmbed(member: Member): CadrizEmbedBuilder {
		val embed = CadrizEmbedBuilder.result("Global Shop", member)
		for ((key, value) in categories)
			embed.addField(value.name, "ID: `$key`\n${value.items.size} items", true)
		return embed
	}
}