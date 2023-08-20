package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

class LocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
	override fun serialize(value: LocalDateTime?, gen: JsonGenerator, provider: SerializerProvider) {
		if (value == null) {
			gen.writeNull()
		} else {
			gen.writeString(value.format(localDateTimeFormatter))
		}
	}
}

class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
	override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDateTime {
		return LocalDateTime.parse(jp.text, localDateTimeFormatter)
	}
}
