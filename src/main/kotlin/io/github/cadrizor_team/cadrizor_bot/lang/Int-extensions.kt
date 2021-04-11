@file:JvmName("IntExtensions")
package io.github.cadrizor_team.cadrizor_bot.lang

import io.github.cadrizor_team.cadrizor_bot.NumberFactory.Companion.from

infix fun Int.multDivide(value: Int) = this / value * value
fun Int.repr() = from(this).repr()