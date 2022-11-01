package kim.hyunsub.division.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kim.hyunsub.division.model.CurrencyCode
import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.model.dto.RestApiRecordShare
import kim.hyunsub.division.repository.entity.Record
import kim.hyunsub.division.repository.entity.RecordShare
import java.time.LocalDateTime

class RecordShareMapperTest : FreeSpec({
	val entity = RecordShare(
		id = "kotest_id",
		recordId = "kotest_recordId",
		userId = "kotest_userId",
		realAmount = 0,
		shouldAmount = 0,
	)

	val dto = RestApiRecordShare(
		id = "kotest_id",
		userId = "kotest_userId",
		realAmount = 0,
		shouldAmount = 0,
	)

	val mapper = RecordShareMapper.mapper

	"convert" {
		mapper.convert(entity) shouldBe dto
		mapper.convert(dto, entity.recordId) shouldBe entity
	}
})
