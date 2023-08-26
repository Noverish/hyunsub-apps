package kim.hyunsub.diary.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.repository.DiaryRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/api/v1/status")
class DiaryStatusController(
	private val diaryRepository: DiaryRepository,
) {
	@GetMapping("/month")
	fun statusMonth(
		user: UserAuth,
		year: Int,
		month: Int,
	): List<LocalDate> {
		val ym = YearMonth.parse("$year-${month.toString().padStart(2, '0')}")

		val start = ym.atDay(1)
		val end = ym.atEndOfMonth()

		return diaryRepository.findDates(user.idNo, start, end)
	}
}
