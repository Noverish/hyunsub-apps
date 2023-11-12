package kim.hyunsub.common.jackson

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime

class OffsetDateTimeTest : FreeSpec({
	val mapper = JacksonConfiguration().objectMapper()

	data class OffsetDateTimeData(
		val date: OffsetDateTime,
	)

	"OffsetDateTime" {
		val params = OffsetDateTimeData(OffsetDateTime.now().withNano(0))
		val json = mapper.writeValueAsString(params)
		val result = mapper.readValue<OffsetDateTimeData>(json)
		result shouldBe params
	}
})
