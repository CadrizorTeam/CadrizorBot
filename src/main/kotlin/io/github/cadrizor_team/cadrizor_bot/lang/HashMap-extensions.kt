package io.github.cadrizor_team.cadrizor_bot.lang

fun <K, V> HashMap<K, V>.setFrom(map: HashMap<K, V>) {
	clear()
	putAll(map)
}

fun <K, V> HashMap<K, V>.setIfNull(key: K, value: V) {
	if (this[key] == null) this[key] = value
}