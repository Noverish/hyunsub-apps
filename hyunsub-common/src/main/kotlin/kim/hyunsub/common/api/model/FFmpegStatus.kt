package kim.hyunsub.common.api.model

data class FFmpegStatus<T>(
	val isRunning: Boolean,
	val status: FFmpegStatusData,
	val data: T,
)
