package kim.hyunsub.video.model.api

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.video.repository.entity.Video
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

data class RestApiVideoEpisode(
	val videoId: String,
	val thumbnailUrl: String,
	val title: String,
) {
	constructor(video: Video) : this(
		videoId = video.id,
		thumbnailUrl = FileUrlConverter.thumbnailUrl(video.thumbnail),
		title = Path(video.path).nameWithoutExtension,
	)
}
