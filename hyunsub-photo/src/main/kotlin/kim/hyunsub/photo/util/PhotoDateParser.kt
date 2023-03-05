package kim.hyunsub.photo.util

import com.fasterxml.jackson.databind.JsonNode
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object PhotoDateParser {
	private val format1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")

	fun parse(exif: JsonNode): OffsetDateTime {
		exif["SubSecDateTimeOriginal"]?.let {
			return parseString(it.textValue())
		}

		throw IllegalArgumentException("Can not parse date from exif")
	}

	fun parseString(str: String): OffsetDateTime {
		return OffsetDateTime.parse(str, format1)
	}
}
