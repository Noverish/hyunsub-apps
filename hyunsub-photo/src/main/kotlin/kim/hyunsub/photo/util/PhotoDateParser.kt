package kim.hyunsub.photo.util

import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object PhotoDateParser {
	private val format1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")
	private val format2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX")

	fun parse(exif: JsonNode, fileName: String): OffsetDateTime {
		val mime = exif["MIMEType"].textValue()
			?: throw RuntimeException("No MIMEType")

		val dateFromExif = if (mime.startsWith("image")) {
			parseImage(exif)
		} else if (mime.startsWith("video")) {
			parseVideo(exif)
		} else {
			throw RuntimeException("Unknown mime - $mime")
		}

		return dateFromExif
			?: parseFromFileName(fileName).atZone(ZoneId.systemDefault()).toOffsetDateTime()
	}

	private fun parseImage(exif: JsonNode): OffsetDateTime? {
		exif["SubSecDateTimeOriginal"]?.let {
			return parseString(it.textValue())
		}
		return null
	}

	private fun parseVideo(exif: JsonNode): OffsetDateTime? {
		exif["CreationDate"]?.let {
			return parseString(it.textValue())
		}
		return null
	}

	private fun parseString(str: String): OffsetDateTime {
		for (format in listOf(format1, format2)) {
			try {
				return OffsetDateTime.parse(str, format)
			} catch (_: DateTimeParseException) {
			}
		}
		throw RuntimeException("Cannot parse date string: $str")
	}

	private fun parseFromFileName(name: String): LocalDateTime {
		Regex("20\\d{6}_\\d{6}_\\d{3}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"))
		}

		Regex("20\\d{6}_\\d{6}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
		}

		Regex("20\\d{6}-\\d{6}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
		}

		Regex("20\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
		}

		Regex("20\\d{12}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
		}

		val str2 = Regex("1\\d{12}").find(name)
		if (str2 != null) {
			return str2.value.toLong()
				.let { Instant.ofEpochMilli(it) }
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime()
		}

		throw RuntimeException("Failed to process file name - $name")
	}
}
