package kim.hyunsub.encode.model

data class FFmpegStatusData(
	val frame: String,
	val fps: String,
	val q: String,
	val size: String,
	val time: String,
	val bitrate: String,
	val speed: String,
) {
	val progress: Int
		get() {
			val matched = Regex("(\\d{2}):(\\d{2}):(\\d{2}).(\\d{2})").matchEntire(time)!!
			val h = matched.groupValues[1].toInt()
			val m = matched.groupValues[2].toInt()
			val s = matched.groupValues[3].toInt()
			return h * 3600 + m * 60 + s
		}
}
