@file:JvmName("TagsExtensions")

package io.github.cadrizor_team.cadrizor_bot.lang

import com.flowpowered.nbt.*
import io.github.cadrizor_team.cadrizor_bot.init.EnchantmentsInit
import io.github.cadrizor_team.cadrizor_bot.roleplay.enchantment.Enchantment

// fun <T : Tag<T>> ListTag<T>.append(tag: T) = ListTag(name, elementType, ArrayList(value).addReturn(tag))
private operator fun CompoundMap.contains(name: String) = containsKey(name)
operator fun CompoundTag.contains(name: String) = name in value

operator fun CompoundTag.get(key: String) = value[key]

@JvmName("getInt")
operator fun CompoundTag.get(pair: Pair<String, Int>): Int {
	val subTag = this[pair.first] as IntTag?
	return subTag?.value ?: pair.second
}

@JvmName("getFloat")
operator fun CompoundTag.get(pair: Pair<String, Float>): Float {
	val subTag = this[pair.first] as FloatTag?
	return subTag?.value ?: pair.second
}

@JvmName("getLong")
operator fun CompoundTag.get(pair: Pair<String, Long>): Long {
	val subTag = this[pair.first] as LongTag?
	return subTag?.value ?: pair.second
}

@JvmName("getBoolean")
operator fun CompoundTag.get(pair: Pair<String, Boolean>): Boolean {
	val subTag = this[pair.first] as ByteTag?
	return subTag?.booleanValue ?: pair.second
}

@JvmName("getString")
operator fun CompoundTag.get(pair: Pair<String, String>): String {
	val subTag = this[pair.first] as StringTag?
	return subTag?.value ?: pair.second
}

@JvmName("setInt")
operator fun CompoundTag.set(name: String, int: Int) {
	value[name] = IntTag(name, int)
}

@JvmName("setFloat")
operator fun CompoundTag.set(name: String, float: Float) {
	value[name] = FloatTag(name, float)
}

@JvmName("setIntArray")
operator fun CompoundTag.set(name: String, intsTag: IntArrayTag) {
	value[name] = intsTag
}

@JvmName("setString")
operator fun CompoundTag.set(name: String, string: String) {
	value[name] = StringTag(name, string)
}

operator fun <T> CompoundMap.plusAssign(tag: Tag<T>) {
	put(tag.name, tag)
}

fun CompoundTag.getEnchants(name: String): HashMap<Enchantment, Int> {
	val map = HashMap<Enchantment, Int>()
	val subTag = value[name] as? CompoundTag
	subTag?.value?.values?.forEach {
		if (it.name in EnchantmentsInit) map[EnchantmentsInit[it.name]!!] = subTag[it.name to 1]
	}
	return map
}

fun CompoundTag.putEnchants(name: String, enchantments: HashMap<Enchantment, Int>) {
	val tag = CompoundTag(name, CompoundMap())
	enchantments.forEach { (enchant, level) -> tag[enchant.id] = level }
	value += tag
}