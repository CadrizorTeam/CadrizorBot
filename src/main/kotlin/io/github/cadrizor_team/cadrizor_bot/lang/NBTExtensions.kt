@file:JvmName("NBTExtensions")
package io.github.cadrizor_team.cadrizor_bot.lang

import com.flowpowered.nbt.stream.NBTInputStream
import com.flowpowered.nbt.stream.NBTOutputStream
import java.io.File

fun File.nbtInput() = NBTInputStream(inputStream())
fun File.nbtOutput() = NBTOutputStream(outputStream())