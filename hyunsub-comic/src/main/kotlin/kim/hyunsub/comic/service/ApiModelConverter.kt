package kim.hyunsub.comic.service

import kim.hyunsub.comic.model.ApiComicDetail
import kim.hyunsub.comic.model.ApiComicEpisodeDetail
import kim.hyunsub.comic.model.ApiComicEpisodePreview
import kim.hyunsub.comic.model.ApiComicPreview
import kim.hyunsub.comic.repository.entity.Comic
import kim.hyunsub.comic.repository.entity.ComicEpisode
import org.springframework.stereotype.Service

@Service
class ApiModelConverter {
	fun convert(comic: Comic) =
		ApiComicPreview(
			id = comic.id,
			title = comic.title,
		)

	fun convert(comic: Comic, episodes: List<ComicEpisode>) =
		ApiComicDetail(
			id = comic.id,
			title = comic.title,
			episodes = episodes.map { convert(it) }
		)

	fun convert(episode: ComicEpisode) =
		ApiComicEpisodePreview(
			order = episode.order,
			title = episode.title.replace(Regex("^\\d+_"), ""),
			length = episode.length,
			regDt = episode.regDt,
		)

	fun convert(episode: ComicEpisode, images: List<String>) =
		ApiComicEpisodeDetail(
			comicId = episode.comicId,
			order = episode.order,
			title = episode.title.replace(Regex("^\\d+_"), ""),
			length = episode.length,
			regDt = episode.regDt,
			images = images,
		)
}
