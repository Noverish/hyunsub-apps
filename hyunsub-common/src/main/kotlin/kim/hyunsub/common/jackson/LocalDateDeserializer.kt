package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

	override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDate {
		return LocalDate.parse(jp.text, formatter)
	}
}
