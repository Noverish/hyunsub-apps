package kim.hyunsub.video.repository.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import kim.hyunsub.common.api.FileUrlConverter
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

data class VideoEpisode(
	val videoId: String,
	val time: Int?,
	@field:JsonIgnore val path: String,
	@field:JsonIgnore val thumbnail: String,
	@field:JsonIgnore val videoSeason: String?,
	val duration: Int,
) {
	val title: String
		get() = Path(path).nameWithoutExtension

	val thumbnailUrl: String
		get() = FileUrlConverter.thumbnailUrl(thumbnail)
}
