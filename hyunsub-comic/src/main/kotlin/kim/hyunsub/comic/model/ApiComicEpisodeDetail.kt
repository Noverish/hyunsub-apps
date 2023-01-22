package kim.hyunsub.comic.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class ApiComicEpisodeDetail(
	val comicId: String,
	val order: Int,
	val title: String,
	val episodeTitle: String,
	val length: Int,
	val regDt: LocalDateTime,
	val images: List<String>,

	@field:JsonInclude(JsonInclude.Include.NON_NULL)
	val history: Int?,
)
