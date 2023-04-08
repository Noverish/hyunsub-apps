package kim.hyunsub.photo.util

import com.fasterxml.jackson.databind.JsonNode
import kim.hyunsub.photo.model.PhotoDateType
import mu.KotlinLogging
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object PhotoDateParser {
	private val log = KotlinLogging.logger { }
	private val odtFormat1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSSXXX")
	private val odtFormat2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")
	private val odtFormat3 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX")
	private val ldtFormat1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSS")
	private val ldtFormat2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
	private val ldtFormat3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	private val nameFormatMap = mapOf(
		"20\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}" to "yyyy-MM-dd-HH-mm-ss",
		"20\\d{6}_\\d{6}_\\d{3}" to "yyyyMMdd_HHmmss_SSS",
		"20\\d{6}_\\d{6}" to "yyyyMMdd_HHmmss",
		"20\\d{6}-\\d{6}" to "yyyyMMdd-HHmmss",
		"20\\d{12}" to "yyyyMMddHHmmss",
	)

	fun parse(exif: JsonNode, fileName: String, millis: Long): Result {
		val mime = exif["MIMEType"].textValue() ?: throw RuntimeException("No MIMEType")

		val dateFromExif = if (mime.startsWith("image")) {
			parseImage(exif, fileName)
		} else if (mime.startsWith("video")) {
			parseVideo(exif, fileName)
		} else {
			throw RuntimeException("Unknown mime - $mime")
		}

		if (dateFromExif != null) {
			return Result(dateFromExif, PhotoDateType.EXIF)
		}

		val dateFromName = parseFromFileName(fileName)
		if (dateFromName != null) {
			return Result(dateFromName, PhotoDateType.NAME)
		}

		val fileDate = OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
		log.debug { "[Parse Photo Date] $fileName: From File Date" }
		return Result(fileDate, PhotoDateType.FILE)
	}

	private fun parseImage(exif: JsonNode, fileName: String): OffsetDateTime? {
		return parseFromExifAsOdt(exif, fileName, "SubSecDateTimeOriginal")
			?: parseFromExifAsLdt(exif, fileName, "SubSecDateTimeOriginal")

			?: parseFromExifAsOdt(exif, fileName, "DateTimeOriginal")
			?: parseFromExifAsLdt(exif, fileName, "DateTimeOriginal")

			?: parseFromExifAsOdt(exif, fileName, "GPSDateTime")
			?: parseFromExifAsOdt(exif, fileName, "TimeStamp")
			?: parseFromExifAsOdt(exif, fileName, "ModifyDate")

			?: parseFromExifAsLdt(exif, fileName, "GPSDateTime")
			?: parseFromExifAsLdt(exif, fileName, "TimeStamp")
			?: parseFromExifAsLdt(exif, fileName, "ModifyDate")
	}

	private fun parseVideo(exif: JsonNode, fileName: String): OffsetDateTime? {
		return parseFromExifAsOdt(exif, fileName, "CreationDate")
	}

	private fun parseFromExifAsOdt(exif: JsonNode, fileName: String, field: String): OffsetDateTime? {
		val str = exif[field]?.textValue() ?: return null
		if (str.startsWith("0000")) {
			return null
		}

		for (format in listOf(odtFormat1, odtFormat2, odtFormat3)) {
			try {
				val result = OffsetDateTime.parse(str, format)
				log.debug { "[Parse Photo Date] $fileName: From $field - $str" }
				return result
			} catch (_: DateTimeParseException) {
			}
		}

		return null
	}

	private fun parseFromExifAsLdt(exif: JsonNode, fileName: String, field: String): OffsetDateTime? {
		val str = exif[field]?.textValue() ?: return null
		if (str.startsWith("0000")) {
			return null
		}

		val offsetStr = listOfNotNull(
			exif["OffsetTime"]?.asText(),
			exif["OffsetTimeOriginal"]?.asText(),
			exif["OffsetTimeDigitized"]?.asText(),
		).firstOrNull()
		val offset = offsetStr?.let { ZoneOffset.of(it) } ?: ZoneId.systemDefault()

		for (format in listOf(ldtFormat1, ldtFormat2, ldtFormat3)) {
			try {
				val result = LocalDateTime.parse(str, format).atZone(offset).toOffsetDateTime()
				log.debug { "[Parse Photo Date] $fileName: From $field - $str $offsetStr" }
				return result
			} catch (_: DateTimeParseException) {
			}
		}

		return null
	}

	private fun parseFromFileName(fileName: String): OffsetDateTime? {
		for ((regex, pattern) in nameFormatMap) {
			val result = Regex(regex).find(fileName)?.value?.let {
				LocalDateTime.parse(it, DateTimeFormatter.ofPattern(pattern))
					.atZone(ZoneId.systemDefault())
					.toOffsetDateTime()
			}

			if (result != null) {
				log.debug { "[Parse Photo Date] $fileName: Name - $result" }
				return result
			}
		}

		return Regex("1\\d{12}").find(fileName)?.value?.toLong()
			?.let {
				Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toOffsetDateTime()
			}
			?.apply {
				log.debug { "[Parse Photo Date] $fileName: Name - $this" }
			}
	}

	data class Result(
		val date: OffsetDateTime,
		val type: PhotoDateType,
	)
}
