package kim.hyunsub.common.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class LocalDateRange(
	val start: LocalDate,
	val end: LocalDate,
) {
	fun toLdtRange() = LocalDateTimeRange(
		start = LocalDateTime.of(start, LocalTime.MIN),
		end = LocalDateTime.of(end, LocalTime.MAX),
	)
}
