package io.github.cadrizor_team.cadrizor_bot.emotes

object DiscordEmotes {
	// http://unicode.org/emoji/charts/full-emoji-list.html
	// https://onlineunicodetools.com/escape-unicode "U-prefixed Surrogate"
	const val BACK = "\ud83d\udd19" // #1363
	const val eject = "\u23cf" // #1410
	const val number0 = "\u0030\ufe0f\u20e3" // #1457
	const val number1 = "\u0031\ufe0f\u20e3"
	const val number2 = "\u0032\ufe0f\u20e3"
	const val number3 = "\u0033\ufe0f\u20e3"
	const val number4 = "\u0034\ufe0f\u20e3"
	const val number5 = "\u0035\ufe0f\u20e3"
	const val number6 = "\u0036\ufe0f\u20e3"
	const val number7 = "\u0037\ufe0f\u20e3"
	const val number8 = "\u0038\ufe0f\u20e3"
	const val number9 = "\u0039\ufe0f\u20e3"
	const val NG = "\ud83c\udd96" // #1483
	const val OK = "\ud83c\udd97" // #1485

	fun addNumberTo(value: String, name: String) = when (name) {
		number0 -> "${value}0"
		number1 -> "${value}1"
		number2 -> "${value}2"
		number3 -> "${value}3"
		number4 -> "${value}4"
		number5 -> "${value}5"
		number6 -> "${value}6"
		number7 -> "${value}7"
		number8 -> "${value}8"
		number9 -> "${value}9"
		else -> value
	}
}