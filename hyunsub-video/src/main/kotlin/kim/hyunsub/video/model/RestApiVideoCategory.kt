package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.VideoCategory

data class RestApiVideoCategory(
	val name: String,
	val displayName: String,
	val iconHtmlClass: String,
	val listHtmlClass: String,
	val itemCss: String,
) {
	constructor(entity: VideoCategory): this(
		name = entity.name,
		displayName = entity.displayName,
		iconHtmlClass = entity.iconHtmlClass,
		listHtmlClass = entity.listHtmlClass,
		itemCss = entity.itemCss,
	)
}
