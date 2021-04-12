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
	assert(this[pair.first] == null || this[pair.first] is IntTag)
	val subTag = this[pair.first] ?: IntTag(pair.first, pair.second)
	return (subTag as IntTag).value
}

@JvmName("getFloat")
operator fun CompoundTag.get(pair: Pair<String, Float>): Float {
	assert(this[pair.first] == null || this[pair.first] is FloatTag)
	val subTag = this[pair.first] ?: FloatTag(pair.first, pair.second)
	return (subTag as FloatTag).value
}

@JvmName("getLong")
operator fun CompoundTag.get(pair: Pair<String, Long>): Long {
	assert(this[pair.first] == null || this[pair.first] is LongTag)
	val subTag = this[pair.first] ?: LongTag(pair.first, pair.second)
	return (subTag as LongTag).value
}

@JvmName("getBoolean")
operator fun CompoundTag.get(pair: Pair<String, Boolean>): Boolean {
	assert(this[pair.first] == null || this[pair.first] is ByteTag)
	val subTag = this[pair.first] ?: ByteTag(pair.first, pair.second)
	return (subTag as ByteTag).booleanValue
}

@JvmName("getString")
operator fun CompoundTag.get(pair: Pair<String, String>): String {
	assert(this[pair.first] == null || this[pair.first] is StringTag)
	val subTag = this[pair.first] ?: StringTag(pair.first, pair.second)
	return (subTag as StringTag).value
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