package kim.hyunsub.comic.model

data class ComicOrganizeSplitParams(
	val title: String,
	val folder: String,
	val excludes: List<String>,
	val dryRun: Boolean,
)
