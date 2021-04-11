package io.github.cadrizor_team.cadrizor_bot.roleplay.crafting

import io.github.cadrizor_team.cadrizor_bot.emotes.CadrizEmotes
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.splitOnLineBreaks
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder
import io.github.cadrizor_team.cadrizor_bot.utils.Utils
import net.dv8tion.jda.api.entities.Member

object CraftingManager {
	internal val crafts = HashMap<String, Recipe>()

	operator fun get(output: String) = crafts[output]

	fun register(recipe: Recipe) {
		this.crafts[recipe.name] = recipe
	}

	fun costs(event: GMREvent, inventory: MemberInventory, output: String) = crafts[output]?.let {
		val costs = it.costs(inventory)
		if (it.canCraft(inventory)) {
			event.channel.sendMessage("$costs\n${CadrizEmotes.Crafting} = Craft").complete()
		} else {
			Utils.sendVolatileMessage(event, costs)
			null
		}
	}

	fun craft(event: GMREvent, output: String, memberInventory: MemberInventory) {
		val member = event.member!!
		val recipe = crafts[output]
		recipe?.let {
			if (!it.craft(memberInventory)) {
				val costs = it.costs(memberInventory).splitOnLineBreaks().run { copyOfRange(1, size) }
				return CadrizEmbedBuilder.result("Cannot craft", member)
						.addField("Crafting", output, true)
						.addField("Crafting Costs", costs.joinToString("\n"), true)
						.setColor(0xff8000)
						.send(event)
			}

			memberInventory.storeData(member)
			CadrizEmbedBuilder.result("Item Crafted", member)
					.addField("Crafting", output, true)
					.send(event)
		}
		CadrizEmbedBuilder.error("Unknown recipe: $output").send(event)
	}

	fun craftSpecial(member: Member, recipe: Recipe, inventory: MemberInventory): Boolean {
		if (!recipe.craft(inventory)) {
			val costs = recipe.costs(inventory).splitOnLineBreaks()
			val newCosts = costs.copyOfRange(1, costs.size)
			CadrizEmbedBuilder.result("Cannot craft", member)
					.addField("Crafting", recipe.name, true)
					.addField("Crafting Costs", newCosts.joinToString("\n"), true)
					.setColor(0xff8000)
					.send(member.guild)
			return false
		}
		inventory.storeData(member)
		return true
	}
}