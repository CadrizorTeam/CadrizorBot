package io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global

import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.ShopItem
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import net.dv8tion.jda.api.entities.Member

class GlobalShopItem @JvmOverloads constructor(
		id: String,
		name: String,
		buyString: String? = null,
		val canBuy: (MemberInventory) -> Boolean = { false },
		val onBuy: (MemberInventory) -> Unit = {},
		sellString: String? = null,
		val canSell: (MemberInventory) -> Boolean = { false },
		val onSell: (MemberInventory) -> Unit = {}
): ShopItem(id, name) {
	val buyString: String? = buyString?.replace("Â".toRegex(), "")
	val sellString: String? = sellString?.replace("Â".toRegex(), "")

	fun getEmbed(category: String, member: Member, inventory: MemberInventory): CadrizEmbedBuilder {
		val embed = CadrizEmbedBuilder.result("Item: $name ($category / Global Shop)", member)
		if (buyString != null) embed.addField("Buy", "${if (canBuy(inventory)) ":white_check_mark: " else ":x: "}$buyString", true)
		if (sellString != null) embed.addField("Sell", "${if (canSell(inventory)) ":white_check_mark: " else ":x: "}$sellString", true)
		return embed
	}
}