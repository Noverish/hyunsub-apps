package kim.hyunsub.comic.repository

import kim.hyunsub.comic.repository.entity.ComicHistory
import kim.hyunsub.comic.repository.entity.ComicHistoryId
import org.springframework.data.jpa.repository.JpaRepository

interface ComicHistoryRepository : JpaRepository<ComicHistory, ComicHistoryId> {
	fun findByUserIdAndComicId(userId: String, comicId: String): List<ComicHistory>
}
