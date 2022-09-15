package kim.hyunsub.common.util

fun getHumanReadableSize(size: Long): String {
	val mb = 1000 * 1000
	val gb = 1000 * 1000 * 1000
	return if (size > gb) {
		val tmp = String.format("%.2f", size.toDouble() / gb.toDouble())
		"$tmp GB"
	} else {
		val tmp = String.format("%.2f", size.toDouble() / mb.toDouble())
		"$tmp MB"
	}
}

fun getHumanReadableBitrate(bitrate: Int): String {
	val kb = 1000
	val mb = 1000 * 1000
	return if (bitrate > mb) {
		val tmp = String.format("%.2f", bitrate.toDouble() / mb.toDouble())
		"$tmp mbp/s"
	} else {
		val tmp = String.format("%.2f", bitrate.toDouble() / kb.toDouble())
		"$tmp kbp/s"
	}
}
