package kim.hyunsub.division.model

import kim.hyunsub.division.repository.entity.Record
import kim.hyunsub.division.repository.entity.RecordShare

data class RecordDetail(
	val record: Record,
	val shares: List<RecordShare>,
)
