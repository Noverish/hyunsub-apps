package kim.hyunsub.common.api.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class YoutubeFormat(
	val formatId: String,
	val formatNote: String,
	val ext: String,
	val fps: Int?,
	val resolution: String,
	val filesize: Int?,
	val abr: Int?,
	val vbr: Int?,
)
