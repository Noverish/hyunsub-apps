package kim.hyunsub.common.api.model

data class FFmpegStatus(
	val isRunning: Boolean,
	val params: FFmpegParams,
	val status: FFmpegStatusData,
)
