package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val localDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

class LocalDateSerializer : StdSerializer<LocalDate>(LocalDate::class.java) {
	override fun serialize(value: LocalDate?, gen: JsonGenerator, provider: SerializerProvider) {
		if (value == null) {
			gen.writeNull()
		} else {
			gen.writeString(value.format(localDateFormatter))
		}
	}
}

class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {
	override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDate {
		return LocalDate.parse(jp.text, localDateFormatter)
	}
}
