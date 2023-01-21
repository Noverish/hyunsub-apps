package kim.hyunsub.comic.service

import kim.hyunsub.comic.model.ApiComicDetail
import kim.hyunsub.comic.model.ApiComicEpisodeDetail
import kim.hyunsub.comic.model.ApiComicEpisodePreview
import kim.hyunsub.comic.model.ApiComicPreview
import kim.hyunsub.comic.repository.entity.Comic
import kim.hyunsub.comic.repository.entity.ComicEpisode
import kim.hyunsub.comic.repository.entity.ComicHistory
import org.springframework.stereotype.Service

@Service
class ApiModelConverter {
	fun convert(comic: Comic) =
		ApiComicPreview(
			id = comic.id,
			title = comic.title,
		)

	fun convert(comic: Comic, episodes: List<ComicEpisode>, histories: List<ComicHistory>): ApiComicDetail {
		val map = histories.associateBy { it.order }

		return ApiComicDetail(
			id = comic.id,
			title = comic.title,
			episodes = episodes.map { convert(it, map[it.order]?.page) }
		)
	}

	fun convert(episode: ComicEpisode, history: Int?) =
		ApiComicEpisodePreview(
			order = episode.order,
			title = episode.title.replace(Regex("^\\d+_"), ""),
			length = episode.length,
			regDt = episode.regDt,
			history = history,
		)

	fun convert(episode: ComicEpisode, images: List<String>, history: Int?) =
		ApiComicEpisodeDetail(
			comicId = episode.comicId,
			order = episode.order,
			title = episode.title.replace(Regex("^\\d+_"), ""),
			length = episode.length,
			regDt = episode.regDt,
			images = images,
			history = history,
		)
}
