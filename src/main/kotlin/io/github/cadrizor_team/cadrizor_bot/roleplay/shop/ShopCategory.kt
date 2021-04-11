package io.github.cadrizor_team.cadrizor_bot.roleplay.shop

abstract class ShopCategory<I : ShopItem> protected constructor(val id: String, val name: String) {
	val items = ArrayList<I>()

	operator fun get(name: String) = items.firstOrNull { it.id == name }
}