package kim.hyunsub.comic.model

import kim.hyunsub.comic.repository.entity.ComicEpisode

data class ComicScanResult(
	val lengthChanged: List<ComicScanResultData>,
	val newEpisodes: List<ComicEpisode>,
)

data class ComicScanResultData(
	val title: String,
	val oldLen: Int,
	val newLen: Int,
)
