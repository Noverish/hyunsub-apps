package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

val offsetDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

class OffsetDateTimeSerializer : StdSerializer<OffsetDateTime>(OffsetDateTime::class.java) {
	override fun serialize(value: OffsetDateTime?, gen: JsonGenerator, provider: SerializerProvider) {
		if (value == null) {
			gen.writeNull()
		} else {
			gen.writeString(value.format(offsetDateTimeFormatter))
		}
	}
}

class OffsetDateTimeDeserializer : StdDeserializer<OffsetDateTime>(OffsetDateTime::class.java) {
	override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): OffsetDateTime {
		return OffsetDateTime.parse(jp.text, localDateFormatter)
	}
}
