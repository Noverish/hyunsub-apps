package kim.hyunsub.comic.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class ApiComicEpisodePreview(
	val order: Int,
	val title: String,
	val length: Int,
	val regDt: LocalDateTime,

	@field:JsonInclude(JsonInclude.Include.NON_NULL)
	val history: Int?,
)
