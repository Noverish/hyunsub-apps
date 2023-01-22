package kim.hyunsub.comic.repository

import kim.hyunsub.comic.repository.entity.ComicEpisode
import kim.hyunsub.comic.repository.entity.ComicEpisodeId
import org.springframework.data.jpa.repository.JpaRepository

interface ComicEpisodeRepository : JpaRepository<ComicEpisode, ComicEpisodeId> {
	fun findByComicId(comicId: String): List<ComicEpisode>
	fun countByComicId(comicId: String): Int
}
