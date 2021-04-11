package io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global

import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.ShopCategory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import net.dv8tion.jda.api.entities.Member

class GlobalShopCategory
private constructor(id: String, name: String, shopItems: ArrayList<GlobalShopItem>) : ShopCategory<GlobalShopItem>(id, name) {
	init {
		items += shopItems
	}

	fun getEmbed(member: Member): CadrizEmbedBuilder {
		val embed = CadrizEmbedBuilder.result("Category: $name (Global Shop)", member)
				.appendDescription("\nID: `$id`")
		var i = 0
		while (i < items.size && i < 20) {
			val shopItem = items[i]
			var desc = "ID: `${shopItem.id}`"
			shopItem.buyString?.let { desc += "\nBuy $it" }
			shopItem.sellString?.let { desc += "\nSell $it" }
			embed.addField(shopItem.name, desc, true)
			i++
		}
		return embed
	}

	class Builder(private val ID: String, private val name: String) {
		private val items: ArrayList<GlobalShopItem> = ArrayList()

		fun addItem(shopItem: GlobalShopItem): Builder {
			items.add(shopItem)
			return this
		}

		fun setItems(vararg shopItems: GlobalShopItem): Builder {
			items.clear()
			items.addAll(listOf(*shopItems))
			return this
		}

		fun resetItems() = items.clear()

		fun build() = GlobalShopCategory(ID, name, items)
	}
}