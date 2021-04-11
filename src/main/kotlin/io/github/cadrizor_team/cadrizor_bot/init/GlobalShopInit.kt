package io.github.cadrizor_team.cadrizor_bot.init

import io.github.cadrizor_team.cadrizor_bot.lang.repr
import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global.GlobalShop
import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global.GlobalShopCategory
import io.github.cadrizor_team.cadrizor_bot.roleplay.shop.global.GlobalShopItem

object GlobalShopInit {
	@JvmStatic
	fun init() {
		val wood1 = GlobalShopItem("wx1", "Wood*1", "3¤", { it.money >= 3 }, {
			it.money -= 3
			it.wood++
		})
		val cobble1 = GlobalShopItem("cx1", "Cobble*1", "5¤", { it.money >= 5 }, {
			it.money -= 5
			it.cobble++
		})
		val stone1 = GlobalShopItem("sx1", "Stone*1", "6¤", { it.money >= 6 }, {
			it.money -= 6
			it.stone++
		})

		val woodCobble = GlobalShopCategory.Builder("woodcob", "Wood/Cobble")
				.setItems(wood1, cobble1, stone1)
				.build()

		val ironOre1 = GlobalShopItem("iox1", "Iron Ore*1", "30¤", { it.money >= 30 }, { mi ->
			mi.money -= 30
			mi.ironOre++
		})
		val ironIngot1 = GlobalShopItem("iix1", "Iron Ingot*1", sellString = "20¤", canSell = { it.ironIngot >= 1 }, onSell = {
			it.money += 20
			it.ironIngot--
		})
		val goldOre1 = GlobalShopItem("gox1", "Gold Ore*1", "40¤", { it.money >= 40 }, {
			it.money -= 40
			it.goldOre++
		})
		val goldIngot1 = GlobalShopItem("gix1", "Gold Ingot*1", sellString = "30¤", canSell = { it.goldIngot >= 1 }, onSell = {
			it.money += 30
			it.goldIngot--
		})

		val minerals = GlobalShopCategory.Builder("minerals", "Minerals")
				.setItems(ironOre1, ironIngot1, goldOre1, goldIngot1)
				.build()

		val diamond1 = GlobalShopItem("dx1", "Diamond*1", "800¤", { mi -> mi.money >= 800 }, { mi ->
			mi.money -= 800
			mi.diamond++
		}, "75¤", { mi -> mi.diamond >= 1 }, { mi ->
			mi.money += 75
			mi.diamond--
		})
		val emerald1 = GlobalShopItem("ex1", "Emerald*1", "1000¤", { mi -> mi.money >= 1000 }, { mi ->
			mi.money -= 1000
			mi.emerald++
		}, "95¤", { mi -> mi.emerald >= 1 }, { mi ->
			mi.money += 95
			mi.emerald--
		})
		val diamerald1 = GlobalShopItem("ex1", "Crushed Diamerald*1", "${1470000.repr()}¤", { it.money >= 1470000 }, {
			it.money -= 1470000
			it.crushedDiamerald++
		}, "${102900.repr()}¤", { it.crushedDiamerald >= 1 }, {
			it.money += 102900
			it.crushedDiamerald--
		})

		val precious = GlobalShopCategory.Builder("precious", "Precious")
				.setItems(diamond1, emerald1, diamerald1)
				.build()

		val endGame = GlobalShopCategory.Builder("end_game", "End Game").build()

		GlobalShop += woodCobble
		GlobalShop += minerals
		GlobalShop += precious
		GlobalShop += endGame
	}
}