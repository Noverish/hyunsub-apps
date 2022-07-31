package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.VideoCategory

data class RestVideoCategory(
	val name: String,
	val displayName: String,
	val htmlClass: String,
) {
	constructor(entity: VideoCategory): this(
		name = entity.name,
		displayName = entity.displayName,
		htmlClass = entity.htmlClass,
	)
}
