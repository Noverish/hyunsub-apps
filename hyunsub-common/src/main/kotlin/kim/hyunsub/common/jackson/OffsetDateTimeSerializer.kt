package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeSerializer : StdSerializer<OffsetDateTime>(OffsetDateTime::class.java) {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

	override fun serialize(value: OffsetDateTime?, gen: JsonGenerator, provider: SerializerProvider) {
		if (value == null) {
			gen.writeNull()
		} else {
			gen.writeString(value.format(formatter))
		}
	}
}
