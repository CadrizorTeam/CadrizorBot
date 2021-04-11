@file:JvmName("StringExtensions")
package io.github.cadrizor_team.cadrizor_bot.lang

fun String.splitOn(regex: String) = split(regex.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
fun String.splitOnLineBreaks() = splitOn("\n")