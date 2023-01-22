package kim.hyunsub.comic.model

import kim.hyunsub.comic.repository.entity.Comic
import kim.hyunsub.comic.repository.entity.ComicEpisode

data class ComicRegisterResult(
	val comic: Comic,
	val episodes: List<ComicEpisode>,
)
