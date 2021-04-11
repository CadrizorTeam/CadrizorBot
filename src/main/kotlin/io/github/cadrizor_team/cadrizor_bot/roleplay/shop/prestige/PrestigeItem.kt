package io.github.cadrizor_team.cadrizor_bot.roleplay.shop.prestige

import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.ShopItem
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory

class PrestigeItem(id: String, name: String, val costs: (Int) -> Int, val upgrade: (Int) -> Int) : ShopItem(id, name) {
	fun buyUpgrade(inventory: MemberInventory, level: Int): Boolean {
		if (level < 0) return false
		if (inventory.prestige.prestige >= costs(level)) {
			inventory.prestige.prestige -= costs(level)

			when (id) {
				"durability" -> inventory.prestige.lvlDurability = level + 1
				"furnace_size" -> inventory.prestige.lvlFurnaceSize = level + 1
				"storage" -> {
					inventory.prestige.lvlStorage = level + 1
					inventory.applyStorageUpgrade()
				}
				else -> throw IllegalStateException("Unknown upgrade $id")
			}
			return true
		}
		return false
	}
}