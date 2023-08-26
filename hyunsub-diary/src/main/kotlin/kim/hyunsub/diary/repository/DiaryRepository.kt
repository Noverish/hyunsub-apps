package kim.hyunsub.diary.repository

import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.diary.repository.entity.DiaryId
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

interface DiaryRepository : JpaRepository<Diary, DiaryId> {
	@Query(
		"""
			SELECT a FROM Diary a
			WHERE a.userId = :userId
				AND (a.content LIKE CONCAT('%', :query, '%') OR a.summary LIKE CONCAT('%', :query, '%'))
			ORDER BY a.date DESC
		"""
	)
	fun search(userId: String, query: String, page: Pageable): List<Diary>

	@Query(
		"""
			SELECT COUNT(a) FROM Diary a
			WHERE a.userId = :userId
				AND (a.content LIKE CONCAT('%', :query, '%') OR a.summary LIKE CONCAT('%', :query, '%'))
		"""
	)
	fun searchCount(userId: String, query: String): Int

	@Query(
		"""
			SELECT a.date FROM Diary a
			WHERE a.userId = :userId AND a.date BETWEEN :start AND :end
		"""
	)
	fun findDates(userId: String, start: LocalDate, end: LocalDate): List<LocalDate>
}

fun DiaryRepository.findByIdOrNull(userId: String, date: LocalDate) = findByIdOrNull(DiaryId(userId, date))
