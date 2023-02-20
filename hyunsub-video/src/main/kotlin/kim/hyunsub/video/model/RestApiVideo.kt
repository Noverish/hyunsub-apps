package kim.hyunsub.video.model

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.video.repository.entity.Video
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

data class RestApiVideo (
	val videoId: String,
	val videoUrl: String,
	val thumbnailUrl: String,
	val title: String,
	val subtitles: List<RestVideoSubtitle>,
	val metadata: RestVideoMetadata?,
) {
	constructor(entity: Video): this(
		videoId = entity.id,
		videoUrl = FileUrlConverter.convertToUrl(entity.path),
		thumbnailUrl = FileUrlConverter.thumbnailUrl(entity.thumbnail),
		title = Path(entity.path).nameWithoutExtension,
		subtitles = emptyList(),
		metadata = null,
	)
}
