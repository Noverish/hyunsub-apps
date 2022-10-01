package kim.hyunsub.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateSerializer : StdSerializer<LocalDate>(LocalDate::class.java) {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

	override fun serialize(value: LocalDate?, gen: JsonGenerator, provider: SerializerProvider) {
		if (value == null) {
			gen.writeNull()
		} else {
			gen.writeString(value.format(formatter))
		}
	}
}
