package kim.hyunsub.photo.model.api

import java.time.OffsetDateTime

data class ApiRescanDateResult(
	val oldId: String,
	val newId: String,
	val oldDate: OffsetDateTime,
	val newDate: OffsetDateTime,
)
