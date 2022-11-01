package kim.hyunsub.division.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kim.hyunsub.division.model.CurrencyCode
import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.repository.entity.Record
import java.time.LocalDateTime

class RecordMapperTest : FreeSpec({
	val date = LocalDateTime.now()

	val entity = Record(
		id = "kotest_id",
		content = "kotest_content",
		location = "kotest_location",
		currency = CurrencyCode.KRW,
		date = date,
		gatheringId = "kotest_gatheringId",
	)

	val dto = RestApiRecord(
		id = "kotest_id",
		content = "kotest_content",
		location = "kotest_location",
		currency = CurrencyCode.KRW,
		date = date,
		shares = emptyList(),
		gatheringId = "kotest_gatheringId",
	)

	val mapper = RecordMapper.mapper

	"convert" {
		mapper.convert(entity) shouldBe dto
		mapper.convert(dto) shouldBe entity
	}
})
