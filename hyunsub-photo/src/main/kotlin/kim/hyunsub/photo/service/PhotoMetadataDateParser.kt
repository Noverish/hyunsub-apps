package kim.hyunsub.photo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.log.Log
import kim.hyunsub.photo.model.RestApiExifDate
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
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
		private val format5 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

		fun parse(str: String): LocalDateTime? {
			val formats = listOf(format1, format2, format3, format4, format5)
			for (format in formats) {
				try  {
					return OffsetDateTime.parse(str, format)
						.withOffsetSameInstant(ZoneOffset.ofHours(9))
						.toLocalDateTime()
				} catch (ex: DateTimeParseException) {

				}

				try {
					return LocalDateTime.parse(str, format)
				} catch (ex: DateTimeParseException) {
					continue
				}
			}
			log.debug("Cannot parse date: {}", str)
			return null
		}
	}

	private val mapper = jacksonObjectMapper()

	fun parse(photo: Photo, metadata: PhotoMetadata): RestApiExifDate {
		val node = mapper.readTree(metadata.data)[0]
		val dateTimeCreated = node.get("DateTimeCreated")?.asText()?.let { parse(it) }
		val subSecCreateDate = node.get("SubSecCreateDate")?.asText()?.let { parse(it) }
		val fileModifyDate = node.get("FileModifyDate")?.asText()?.let { parse(it) }
		val modifyDate = node.get("ModifyDate")?.asText()?.let { parse(it) }
		val createDate = node.get("CreateDate")?.asText()?.let { parse(it) }
		val creationDate = node.get("CreationDate")?.asText()?.let { parse(it) }
		val dateTimeOriginal = node.get("DateTimeOriginal")?.asText()?.let { parse(it) }
		val timeStamp = node.get("TimeStamp")?.asText()?.let { parse(it) }
		val gpsDateTime = node.get("GPSDateTime")?.asText()?.let { parse(it) }
		val nameDate = parse(Path(metadata.path).nameWithoutExtension)

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
}
