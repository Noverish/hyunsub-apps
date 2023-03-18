package kim.hyunsub.photo.util

import com.fasterxml.jackson.databind.JsonNode
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object PhotoDateParser {
	private val format1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")
	private val format2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX")

	fun parse(exif: JsonNode): OffsetDateTime {
		val mime = exif["MIMEType"].textValue()
			?: throw RuntimeException("No MIMEType")

		if (mime.startsWith("image")) {
			return parseImage(exif)
		} else if (mime == "video/quicktime") {
			return parseVideo(exif)
		}

		throw RuntimeException("Unknown File Type")
	}

	private fun parseImage(exif: JsonNode): OffsetDateTime {
		exif["SubSecDateTimeOriginal"]?.let {
			return parseString(it.textValue())
		}
		throw RuntimeException("Failed to process photo")
	}

	private fun parseVideo(exif: JsonNode): OffsetDateTime {
		exif["CreationDate"]?.let {
			return parseString(it.textValue())
		}
		throw RuntimeException("Failed to process video")
	}

	private fun parseString(str: String): OffsetDateTime {
		for(format in listOf(format1, format2)) {
			try {
				return OffsetDateTime.parse(str, format)
			} catch (_: DateTimeParseException) {

			}
		}
		throw RuntimeException("Cannot parse date string: $str")
	}
}
