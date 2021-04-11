package io.github.cadrizor_team.cadrizor_bot.init

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.cadrizor_team.cadrizor_bot.Main
import io.github.cadrizor_team.cadrizor_bot.admin.*
import io.github.cadrizor_team.cadrizor_bot.commands.*
import io.github.cadrizor_team.cadrizor_bot.lang.GMREvent
import io.github.cadrizor_team.cadrizor_bot.lang.isOwner
import io.github.cadrizor_team.cadrizor_bot.utils.CadrizEmbedBuilder

object CommandsInit {
	@JvmStatic
	fun init() {
		registerCommand(ArmorCraftCommand.armorCraft())
		registerCommand(BonusesCommand.bonuses())
		registerCommand(CraftCommand.craft())
		registerCommand(EnchantCommand.enchant())
		registerCommand(GShopCommand.gshop())
		registerCommand(HarvestInfoCommand.harvestInfo())
		registerCommand(InvCommand.inv())
		registerCommand(MineCommand.mine())
		registerCommand(MobsCommand.mobs())
		registerCommand(MoneyCommand.money())
		registerCommand(PayCommand.pay())
		registerCommand(PicksCommand.picks())
		registerCommand(PrestigeCommand.prestige())
		registerCommand(PShopCommand.pshop())
		registerCommand(SmeltCommand.smelt())
		registerCommand(StorageCommand.storage())
		registerCommand(SwordCommand.sword())
		registerCommand(SwordInfoCommand.swordInfo())
		registerCommand(WitherCommand.wither())
		registerCommand(WoodCommand.wood())
		registerCommand(WorkbenchCommand.workbench())
		registerCommand(XPCommand.xp())

		registerHelpCommand()
		if (Main.dev) registerCommand(DumpCommand.dump())
		else registerCommand(DumpCommand.dump().requires(GMREvent::isOwner))

		registerAdminCommand(EditBonusesCommand.editBonuses())
		registerAdminCommand(FSmeltCommand.fsmelt())
		registerAdminCommand(GiveCommand.give())
		registerAdminCommand(IgnoreCommand.ignore())

		registerAdminCommand(ResetCommand.reset())

		registerAdminCommand(StopCommand.stop())
	}

	private fun registerHelpCommand() = registerCommand(literal<GMREvent>("help")
			.executes {
				CadrizEmbedBuilder.create()
						.setTitle("Cadrizor Bot - Help")
						.appendDescription((if (Main.dev) "Development " else "") + "Version ${Main.VERSION}")
						.addField("Links", "[『 Help 』](${Main.PROJECT_URL})\n[『 Issues 』](${Main.PROJECT_URL}-issues/)", true)
						.send(it.source)
				1
			})!!

	private fun registerCommand(builder: LiteralArgumentBuilder<GMREvent>) = Main.USER_DISPATCHER.register(builder)

	private fun registerAdminCommand(builder: LiteralArgumentBuilder<GMREvent>) = Main.ADMIN_DISPATCHER.register(builder)
}