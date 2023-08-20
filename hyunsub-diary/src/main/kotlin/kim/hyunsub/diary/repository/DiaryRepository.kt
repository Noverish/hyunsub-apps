package kim.hyunsub.diary.repository

import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.diary.repository.entity.DiaryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

interface DiaryRepository : JpaRepository<Diary, DiaryId> {
	fun findByUserIdAndContentContains(userId: String, query: String): List<Diary>
}

fun DiaryRepository.findByIdOrNull(userId: String, date: LocalDate) = findByIdOrNull(DiaryId(userId, date))
