package io.github.cadrizor_team.cadrizor_bot.storage

import com.flowpowered.nbt.CompoundMap
import com.flowpowered.nbt.CompoundTag
import io.github.cadrizor_team.cadrizor_bot.lang.nbtInput
import io.github.cadrizor_team.cadrizor_bot.lang.nbtOutput
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

object BonusesStorage {
	internal val bonusFile: File
		get() {
			val bonusFile = File("data/bonus.dat")
			if (!bonusFile.exists()) {
				try {
					FileUtils.forceMkdir(bonusFile.parentFile)
					FileUtils.touch(bonusFile)
					bonusFile.nbtOutput().use {
						val tag = CompoundTag("bonuses", CompoundMap())
						Bonuses.serialize(tag)
						it.writeTag(tag)
					}
				} catch (e: IOException) {
					e.printStackTrace()
				}
			}
			return bonusFile
		}

	@JvmStatic
	fun retrieveBonuses() {
		try {
			bonusFile.nbtInput().use {
				val bonusesITag = it.readTag()
				val bonusesCompoundTag = if (bonusesITag is CompoundTag) bonusesITag else CompoundTag("bonuses", CompoundMap())
				if (bonusesCompoundTag.value.isEmpty()) Bonuses.serialize(bonusesCompoundTag)
				Bonuses.deserialize(bonusesCompoundTag)
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	@JvmStatic
	fun storeBonuses() {
		val bonusesCompundTag = CompoundTag("bonuses", CompoundMap())
		Bonuses.serialize(bonusesCompundTag)

		try {
			bonusFile.nbtOutput().use { it.writeTag(bonusesCompundTag) }
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}