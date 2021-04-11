package io.github.cadrizor_team.cadrizor_bot.roleplay.shop.prestige

import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.Shop
import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.ShopCategory

object PrestigeShop : Shop<PrestigeItem, PrestigeShop.Prestige>() {
	init {
		categories["prestige"] = Prestige()
	}

	val prestige get() = categories["prestige"]!!

	class Prestige internal constructor() : ShopCategory<PrestigeItem>("prestige", "Prestige") {
		init {
			items += PrestigeItem("storage", "Storage Upgrade", { 10 * 2 * (it + 1) }) {
				var storage = 100
				for (i in 1..it) {
					storage *= 3
					storage /= 2
				}
				storage
			}

			items += PrestigeItem("durability", "Durability Upgrade", { 2 * (it + 1) }) { 100 + 5 * it }

			items += PrestigeItem("furnace_size", "Furnace Size Upgrade", { 15 * 2 * (it + 1) }) {
				var size = 100
				for (i in 1..it) size *= 2
				size
			}
		}
	}
}