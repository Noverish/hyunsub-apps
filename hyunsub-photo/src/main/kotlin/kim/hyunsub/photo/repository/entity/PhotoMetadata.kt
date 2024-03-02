package kim.hyunsub.photo.repository.entity

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.LocalDateTime

data class PhotoMetadata(
	val photoId: String,
	val subSecDateTimeOriginal: String?,
	val dateTimeOriginal: String?,
	val gpsDateTime: String?,
	val timeStamp: String?,
	val modifyDate: String?,
	val creationDate: String?,
	val offsetTime: String?,
	val offsetTimeOriginal: String?,
	val offsetTimeDigitized: String?,
	val raw: String,
	val date: LocalDateTime,
) {
	companion object {
		private val mapper = jacksonObjectMapper()

		fun from(photoId: String, node: JsonNode): PhotoMetadata {
			return PhotoMetadata(
				photoId = photoId,
				subSecDateTimeOriginal = node["SubSecDateTimeOriginal"]?.textValue(),
				dateTimeOriginal = node["DateTimeOriginal"]?.textValue(),
				gpsDateTime = node["GPSDateTime"]?.textValue(),
				timeStamp = node["TimeStamp"]?.textValue(),
				modifyDate = node["ModifyDate"]?.textValue(),
				creationDate = node["CreationDate"]?.textValue(),
				offsetTime = node["OffsetTime"]?.textValue(),
				offsetTimeOriginal = node["OffsetTimeOriginal"]?.textValue(),
				offsetTimeDigitized = node["OffsetTimeDigitized"]?.textValue(),
				raw = mapper.writeValueAsString(node),
				date = LocalDateTime.now(),
			)
		}
	}
}
