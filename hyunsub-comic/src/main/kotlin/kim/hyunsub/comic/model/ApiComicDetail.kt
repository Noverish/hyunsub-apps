package kim.hyunsub.comic.model

data class ApiComicDetail(
	val id: String,
	val title: String,
	val episodes: List<ApiComicEpisodePreview>,
)
