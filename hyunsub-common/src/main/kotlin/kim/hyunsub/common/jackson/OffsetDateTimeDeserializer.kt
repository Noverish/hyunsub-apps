package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeDeserializer : StdDeserializer<OffsetDateTime>(OffsetDateTime::class.java) {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

	override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): OffsetDateTime {
		return OffsetDateTime.parse(jp.text, formatter)
	}
}
