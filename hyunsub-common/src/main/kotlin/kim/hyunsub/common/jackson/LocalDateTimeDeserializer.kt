package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

	override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDateTime {
		return LocalDateTime.parse(jp.text, formatter)
	}
}
