package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.log.Log
import kim.hyunsub.photo.model.RestApiExifDate
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import org.springframework.stereotype.Service
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

@Service
class PhotoMetadataDateParser {
	companion object : Log {
		private val format1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX")
		private val format2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSXXX")
		private val format3 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss.SSSS")
		private val format4 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
	}

	private val mapper = jacksonObjectMapper()

	fun parse(photo: Photo, metadata: PhotoMetadata): RestApiExifDate {
		val node = mapper.readTree(metadata.data)[0]

		val offset = listOfNotNull(
			node["OffsetTime"]?.asText(),
			node["OffsetTimeOriginal"]?.asText(),
			node["OffsetTimeDigitized"]?.asText(),
		).firstOrNull()

		val dateTimeCreated = node["DateTimeCreated"]?.asText()?.let { parse(it, offset) }
		val subSecCreateDate = node["SubSecCreateDate"]?.asText()?.let { parse(it, offset) }
		val fileModifyDate = node["FileModifyDate"]?.asText()?.let { parse(it, offset) }
		val modifyDate = node["ModifyDate"]?.asText()?.let { parse(it, offset) }
		val createDate = node["CreateDate"]?.asText()?.let { parse(it, offset) }
		val creationDate = node["CreationDate"]?.asText()?.let { parse(it, offset) }
		val dateTimeOriginal = node["DateTimeOriginal"]?.asText()?.let { parse(it, offset) }
		val timeStamp = node["TimeStamp"]?.asText()?.let { parse(it, offset) }
		val gpsDateTime = node["GPSDateTime"]?.asText()?.let { parse(it, offset) }
		val nameDate = parseFromFileName(Path(metadata.path).nameWithoutExtension)

		return RestApiExifDate(
			photoId = photo.id,
			name = Path(metadata.path).name,
			date = photo.date,

			nameDate = nameDate,
			dateTimeCreated = dateTimeCreated,
			subSecCreateDate = subSecCreateDate,
			fileModifyDate = fileModifyDate,
			modifyDate = modifyDate,
			createDate = createDate,
			creationDate = creationDate,
			dateTimeOriginal = dateTimeOriginal,
			timeStamp = timeStamp,
			gpsDateTime = gpsDateTime,
		)
	}

	fun parse(str: String, offset: String? = null): LocalDateTime? {
		if (str.startsWith("0000")) {
			return null
		}

		val systemOffset = ZonedDateTime.now().offset

		for (format in listOf(format1, format2)) {
			try {
				return OffsetDateTime.parse(str, format)
					.withOffsetSameInstant(systemOffset)
					.toLocalDateTime()
			} catch (_: DateTimeParseException) {

			}
		}

		for (format in listOf(format3, format4)) {
			try {
				return if (offset != null) {
					val ldt = LocalDateTime.parse(str, format)
					val zoneOffset = ZoneOffset.of(offset)
					OffsetDateTime.of(ldt, zoneOffset)
						.withOffsetSameInstant(systemOffset)
						.toLocalDateTime()
				} else {
					LocalDateTime.parse(str, format)
				}
			} catch (_: DateTimeParseException) {

			}
		}

		log.debug("Cannot parse date: str={}, offset={}", str, offset)
		return null
	}

	fun parseFromFileName(name: String): LocalDateTime? {
		Regex("20\\d{6}_\\d{6}_\\d{3}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"))
		}

		Regex("20\\d{6}_\\d{6}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
		}

		Regex("20\\d{6}-\\d{6}").find(name)?.value?.let {
			return LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
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

		return null
	}

	fun determineDate(metadata: PhotoMetadata): LocalDateTime? {
		val node = mapper.readTree(metadata.data)[0]

		val offset = listOfNotNull(
			node["OffsetTime"]?.asText(),
			node["OffsetTimeOriginal"]?.asText(),
			node["OffsetTimeDigitized"]?.asText(),
		).firstOrNull()

		return node["SubSecDateTimeOriginal"]?.asText()?.let { parse(it, offset) }
			?: node["SubSecCreateDate"]?.asText()?.let { parse(it, offset) }
			?: node["DateTimeOriginal"]?.asText()?.let { parse(it, offset) }
			?: node["CreateDate"]?.asText()?.let { parse(it, offset) }
			?: node["FileModifyDate"]?.asText()?.let { parse(it, offset) }
	}
}
