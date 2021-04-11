package io.github.cadrizor_team.cadrizor_bot.roleplay.shop

abstract class Shop<I : ShopItem, C : ShopCategory<I>> {
	protected val categories = HashMap<String, C>()

	operator fun get(key: String) = categories[key]

	operator fun plusAssign(category: C) {
		categories[category.id] = category
	}

	operator fun minusAssign(key: String) {
		categories.remove(key)
	}
}