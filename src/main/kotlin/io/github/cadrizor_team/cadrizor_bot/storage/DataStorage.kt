package io.github.cadrizor_team.cadrizor_bot.storage

import com.flowpowered.nbt.CompoundMap
import com.flowpowered.nbt.CompoundTag
import io.github.cadrizor_team.cadrizor_bot.lang.contains
import io.github.cadrizor_team.cadrizor_bot.lang.get
import io.github.cadrizor_team.cadrizor_bot.lang.nbtInput
import io.github.cadrizor_team.cadrizor_bot.lang.nbtOutput
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberInventory
import io.github.cadrizor_team.cadrizor_bot.storage.inventory.MemberPrestigeInventory
import io.github.cadrizor_team.cadrizor_bot.lang.plusAssign
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

object DataStorage {
	@JvmStatic
	fun retrieveData(guild: Guild, member: Member): GOCResult {
		try {
			guild.dataStorageFile.nbtInput().use {
				val memberCompoundTag: CompoundTag
				var newMember = false

				val guildsITag = it.readTag()
				val guildCompoundTag = if (guildsITag is CompoundTag) guildsITag else CompoundTag("guild", CompoundMap())
				var membersCompoundTag = if (guildCompoundTag["members"] != null) guildCompoundTag["members"] as CompoundTag else null
				if (membersCompoundTag == null) {
					membersCompoundTag = CompoundTag("members", CompoundMap())
					guildCompoundTag.value += membersCompoundTag
				}
				if ("m${member.user.id}" !in membersCompoundTag) {
					memberCompoundTag = CompoundTag("m${member.user.id}", CompoundMap())
					val inv = MemberInventory()
					inv.prestige = MemberPrestigeInventory()
					inv.serialize(memberCompoundTag)
					membersCompoundTag.value += memberCompoundTag
					newMember = true
				} else
					memberCompoundTag = (membersCompoundTag["m${member.user.id}"] as CompoundTag).clone()

				try {
					guild.dataStorageFile.nbtOutput().use { nos -> nos.writeTag(guildCompoundTag) }
				} catch (e1: IOException) {
					e1.printStackTrace()
				}
				return GOCResult(newMember, memberCompoundTag)
			}
		} catch (e: IOException) {
			val guildCompoundTag = CompoundTag("guild", CompoundMap())
			val membersCompoundTag = CompoundTag("members", CompoundMap())
			guildCompoundTag.value += membersCompoundTag

			val memberCompoundTag = CompoundTag("m${member.user.id}", CompoundMap())
			val inv = MemberInventory()
			inv.prestige = MemberPrestigeInventory()
			inv.serialize(memberCompoundTag)
			membersCompoundTag.value += memberCompoundTag

			try {
				guild.dataStorageFile.nbtOutput().use { it.writeTag(guildCompoundTag) }
			} catch (e1: IOException) {
				e1.printStackTrace()
			}

			return GOCResult(true, memberCompoundTag)
		}
	}

	@JvmStatic
	fun storeData(member: Member, memberCompoundTag: CompoundTag) {
		try {
			member.guild.dataStorageFile.nbtInput().use {
				val guildCompoundTag = it.readTag() as CompoundTag
				val membersCompoundTag = guildCompoundTag["members"] as CompoundTag
				membersCompoundTag.value.replace("m${member.user.id}", memberCompoundTag)

				try {
					member.guild.dataStorageFile.nbtOutput().use { nos -> nos.writeTag(guildCompoundTag) }
				} catch (e1: IOException) {
					e1.printStackTrace()
				}
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	internal val Guild.dataStorageFile get() = with (File("data/g${id}.dat")) {
		if (!exists()) {
			try {
				FileUtils.forceMkdir(parentFile)
				FileUtils.touch(this)
				nbtOutput().use { it.writeTag(CompoundTag("guild", CompoundMap())) }
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		return@with this
	}

	class GOCResult(val isCreated: Boolean, val memberTag: CompoundTag)
}