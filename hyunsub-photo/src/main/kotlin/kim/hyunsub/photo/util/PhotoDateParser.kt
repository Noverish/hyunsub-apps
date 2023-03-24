package kim.hyunsub.photo.util

import com.fasterxml.jackson.databind.JsonNode
import mu.KotlinLogging
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object PhotoDateParser {
	private val log = KotlinLogging.logger { }
	private val format1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")
	private val format2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX")
	private val nameFormatMap = mapOf(
		"20\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}" to "yyyy-MM-dd-HH-mm-ss",
		"20\\d{6}_\\d{6}_\\d{3}" to "yyyyMMdd_HHmmss_SSS",
		"20\\d{6}_\\d{6}" to "yyyyMMdd_HHmmss",
		"20\\d{6}-\\d{6}" to "yyyyMMdd-HHmmss",
		"20\\d{12}" to "yyyyMMddHHmmss",
	)

	fun parse(exif: JsonNode, fileName: String): OffsetDateTime {
		val mime = exif["MIMEType"].textValue() ?: throw RuntimeException("No MIMEType")

		val dateFromExif = if (mime.startsWith("image")) {
			parseImage(exif, fileName)
		} else if (mime.startsWith("video")) {
			parseVideo(exif, fileName)
		} else {
			throw RuntimeException("Unknown mime - $mime")
		}

		if (dateFromExif != null) {
			return dateFromExif
		}

		val dateFromName = parseFromFileName(fileName)?.atZone(ZoneId.systemDefault())?.toOffsetDateTime()
		if (dateFromName != null) {
			log.debug { "[Parse Photo Date] $fileName: Name - $dateFromName" }
			return dateFromName
		}

		val modifyDate = parseFromExif(exif, fileName, "FileModifyDate")
		if (modifyDate != null) {
			return modifyDate
		}

		log.debug { "[Parse Photo Date] $fileName: Now" }
		return OffsetDateTime.now()
	}

	private fun parseImage(exif: JsonNode, fileName: String): OffsetDateTime? {
		return parseFromExif(exif, fileName, "SubSecDateTimeOriginal")
			?: parseFromExif(exif, fileName, "DateTimeOriginal")
	}

	private fun parseVideo(exif: JsonNode, fileName: String): OffsetDateTime? {
		return parseFromExif(exif, fileName, "CreationDate")
	}

	private fun parseFromExif(exif: JsonNode, fileName: String, field: String): OffsetDateTime? {
		val str = exif[field]?.textValue() ?: return null

		for (format in listOf(format1, format2)) {
			try {
				val result = OffsetDateTime.parse(str, format)
				log.debug { "[Parse Photo Date] $fileName: From $field - $str" }
				return result
			} catch (_: DateTimeParseException) {
			}
		}
		return null
	}

	private fun parseFromFileName(fileName: String): LocalDateTime? {
		for ((regex, pattern) in nameFormatMap) {
			val result = Regex(regex).find(fileName)?.value?.let {
				LocalDateTime.parse(it, DateTimeFormatter.ofPattern(pattern))
			}

			if (result != null) {
				return result
			}
		}

		return Regex("1\\d{12}").find(fileName)?.value?.toLong()?.let {
			Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
		}
	}
}
