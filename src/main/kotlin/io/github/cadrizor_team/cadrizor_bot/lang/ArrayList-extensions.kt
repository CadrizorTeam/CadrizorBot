@file:JvmName("ArrayListExtensions")
package io.github.cadrizor_team.cadrizor_bot.lang

fun <E> ArrayList<E>.addReturn(element: E): ArrayList<E> {
	add(element)
	return this
}