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
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.roundToInt

object PhotoDateParser {
	private val log = KotlinLogging.logger { }
	private val odtFormat1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSSXXX")
	private val odtFormat2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")
	private val odtFormat3 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX")
	private val ldtFormat1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSS")
	private val ldtFormat2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSS")
	private val ldtFormat3 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
	private val ldtFormat4 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	private val ldtFormat5 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")
	private val nameFormatMap = mapOf(
		"20\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}" to "yyyy-MM-dd-HH-mm-ss",
		"20\\d{2}-\\d{2}-\\d{2} \\d{2}.\\d{2}.\\d{2}" to "yyyy-MM-dd HH.mm.ss",
		"20\\d{2}-\\d{2}-\\d{2}-\\d{6}" to "yyyy-MM-dd-HHmmss",
		"20\\d{6}_\\d{6}_\\d{3}" to "yyyyMMdd_HHmmss_SSS",
		"20\\d{6}_\\d{4}_\\d{2}_\\d{3}" to "yyyyMMdd_HHmm_ss_SSS",
		"20\\d{6}_\\d{9}" to "yyyyMMdd_HHmmssSSS",
		"20\\d{6}_\\d{6}" to "yyyyMMdd_HHmmss",
		"20\\d{6}-\\d{6}" to "yyyyMMdd-HHmmss",
		"20\\d{12}" to "yyyyMMddHHmmss",
	)

	fun parse(exif: JsonNode, fileName: String, millis: Long): Result {
		val mime = exif["MIMEType"].textValue() ?: throw RuntimeException("No MIMEType")

		val dateFromExif = if (mime.startsWith("image")) {
			parseImage(exif)
		} else if (mime.startsWith("video")) {
			parseVideo(exif)
		} else {
			throw RuntimeException("Unknown mime - $mime")
		}

		if (dateFromExif != null) {
			val date = dateFromExif.data
			log.debug { "[Parse Photo Date] $fileName: $date from EXIF - ${dateFromExif.sourceFields}, ${dateFromExif.sourceValues}" }
			return Result(date, PhotoDateType.EXIF)
		}

		val dateFromName = parseFromFileName(fileName)
		if (dateFromName != null) {
			val date = dateFromName.data
			log.debug { "[Parse Photo Date] $fileName: $date from NAME - ${dateFromName.sourceFields}, ${dateFromName.sourceValues}" }
			return Result(date, PhotoDateType.NAME)
		}

		val date = OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
		log.debug { "[Parse Photo Date] $fileName: $date from FILE - $millis" }
		return Result(date, PhotoDateType.FILE)
	}

	private fun parseImage(exif: JsonNode): ParseResult<OffsetDateTime>? {
		return parseFromExifFromOdt(exif, "SubSecDateTimeOriginal")
			?: parseScenario2(exif)
			?: parseFromExifFromLdt(exif, "SubSecDateTimeOriginal")

			?: parseFromExifFromOdt(exif, "DateTimeOriginal")
			?: parseFromExifFromLdt(exif, "DateTimeOriginal")

			?: parseFromExifFromOdt(exif, "GPSDateTime")
			?: parseFromExifFromOdt(exif, "TimeStamp")
			?: parseFromExifFromOdt(exif, "ModifyDate")

			?: parseFromExifFromLdt(exif, "GPSDateTime")
			?: parseFromExifFromLdt(exif, "TimeStamp")
			?: parseFromExifFromLdt(exif, "ModifyDate")
	}

	private fun parseVideo(exif: JsonNode): ParseResult<OffsetDateTime>? {
		return parseFromExifFromOdt(exif, "CreationDate")
			?: parseScenario1(exif)
	}

	private fun parseFromExifFromOdt(exif: JsonNode, field: String): ParseResult<OffsetDateTime>? {
		val str = exif[field]?.textValue() ?: return null
		if (str.startsWith("0000") || str.startsWith("1970")) {
			return null
		}

		for (format in listOf(odtFormat1, odtFormat2, odtFormat3)) {
			try {
				return ParseResult(
					sourceFields = listOf(field),
					sourceValues = listOf(str),
					data = OffsetDateTime.parse(str, format),
				)
			} catch (_: DateTimeParseException) {
			}
		}

		return null
	}

	private fun parseFromExifFromLdt(exif: JsonNode, field: String): ParseResult<OffsetDateTime>? {
		val str = exif[field]?.textValue() ?: return null
		if (str.startsWith("0000") || str.startsWith("1970")) {
			return null
		}

		val offsetResult = parseOffset(exif)
		val offset = offsetResult?.data ?: ZoneId.systemDefault()

		for (format in listOf(ldtFormat1, ldtFormat2, ldtFormat3, ldtFormat4, ldtFormat5)) {
			try {
				val data = LocalDateTime.parse(str, format).atZone(offset).toOffsetDateTime()
				return ParseResult(
					sourceFields = (offsetResult?.sourceFields ?: emptyList()) + listOf(field),
					sourceValues = (offsetResult?.sourceValues ?: emptyList()) + listOf(str),
					data = data,
				)
			} catch (_: DateTimeParseException) {
			}
		}

		return null
	}

	private fun parseFromFileName(fileName: String): ParseResult<OffsetDateTime>? {
		for ((regex, pattern) in nameFormatMap) {
			val str = Regex(regex).find(fileName)?.value ?: continue

			val data = LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern))
				.atZone(ZoneId.systemDefault())
				.toOffsetDateTime()

			return ParseResult(
				sourceFields = listOf(pattern),
				sourceValues = listOf(),
				data = data
			)
		}

		val str = Regex("1\\d{12}").find(fileName)?.value ?: return null
		val data = Instant.ofEpochMilli(str.toLong()).atZone(ZoneId.systemDefault()).toOffsetDateTime()

		return ParseResult(
			sourceFields = listOf("millis"),
			sourceValues = listOf(str),
			data = data
		)
	}

	private fun parseOffset(exif: JsonNode): ParseResult<ZoneOffset>? {
		val fields1 = listOf("OffsetTimeOriginal", "OffsetTime")
		for (field in fields1) {
			val value = exif[field]?.asText() ?: continue
			return ParseResult(
				sourceFields = listOf(field),
				sourceValues = listOf(value),
				data = ZoneOffset.of(value),
			)
		}

		val fields2 = listOf("TimeZone")
		for (field in fields2) {
			val value = exif[field]?.asInt() ?: continue
			return ParseResult(
				sourceFields = listOf(field),
				sourceValues = listOf(value.toString()),
				data = ZoneOffset.ofHours(value / 60),
			)
		}

		return null
	}

	private fun parseDateAsLdt(exif: JsonNode, field: String): LocalDateTime? {
		val str = exif[field]?.textValue() ?: return null
		if (str.startsWith("0000") || str.startsWith("1970")) {
			return null
		}

		for (format in listOf(ldtFormat1, ldtFormat2, ldtFormat3, ldtFormat4)) {
			try {
				return LocalDateTime.parse(str, format)
			} catch (_: DateTimeParseException) {
			}
		}

		return null
	}

	private fun parseScenario1(exif: JsonNode): ParseResult<OffsetDateTime>? {
		val offset = parseOffset(exif)
		if (offset != null) {
			return null
		}

		val odt = parseDateAsLdt(exif, "CreateDate")
			?.atOffset(ZoneOffset.UTC)
			?.atZoneSameInstant(ZoneId.systemDefault())
			?.toOffsetDateTime()
			?: return null

		return ParseResult(
			sourceFields = listOf("CreateDate"),
			sourceValues = listOf(exif["CreateDate"].textValue()),
			data = odt,
		)
	}

	/**
	 * `SubSecDateTimeOriginal`이나 `DateTimeOriginal`이 LocalDateTime 포맷이고
	 * `GPSDateTime`이 UTC 포맷으로 존재할 때
	 */
	private fun parseScenario2(exif: JsonNode): ParseResult<OffsetDateTime>? {
		val gpsDateTime = parseFromExifFromOdt(exif, "GPSDateTime")?.data ?: return null
		if (gpsDateTime.offset.totalSeconds != 0) {
			return null
		}
		val gpsDateTimeLdt = gpsDateTime.toLocalDateTime()

		for (field in listOf("SubSecDateTimeOriginal", "DateTimeOriginal")) {
			val dateTimeOriginal = parseDateAsLdt(exif, field) ?: continue

			val diffSeconds = ChronoUnit.SECONDS.between(gpsDateTimeLdt, dateTimeOriginal)
			val diffHour = (diffSeconds.toDouble() / 3600).roundToInt()
			if (abs(diffHour) > 18) {
				return null
			}

			val data = dateTimeOriginal.atOffset(ZoneOffset.ofHours(diffHour))

			return ParseResult(
				sourceFields = listOf(field, "GPSDateTime"),
				sourceValues = listOf(exif[field].textValue(), exif["GPSDateTime"].textValue()),
				data = data,
			)
		}

		return null
	}

	data class ParseResult<T>(
		val sourceFields: List<String>,
		val sourceValues: List<String>,
		val data: T,
	)

	data class Result(
		val date: OffsetDateTime,
		val type: PhotoDateType,
	)
}
