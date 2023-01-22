package kim.hyunsub.comic.model

data class ComicOrganizeConsolidateResult(
	val rename: List<String>,
	val renameFolder: List<String>,
	val removes: List<String>,
)
