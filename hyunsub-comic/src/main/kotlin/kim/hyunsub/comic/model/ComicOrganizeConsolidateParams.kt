package kim.hyunsub.comic.model

data class ComicOrganizeConsolidateParams(
	val title: String,
	val dryRun: Boolean,
	val pattern: String,
	val suffix: String,
)
