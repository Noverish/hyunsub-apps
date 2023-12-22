package kim.hyunsub.photo.repository.entity

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "photo_metadata")
data class PhotoMetadata(
	@Id
	val photoId: String,

	@Column
	val SubSecDateTimeOriginal: String?,

	@Column
	val DateTimeOriginal: String?,

	@Column
	val GPSDateTime: String?,

	@Column
	val TimeStamp: String?,

	@Column
	val ModifyDate: String?,

	@Column
	val CreationDate: String?,

	@Column
	val OffsetTime: String?,

	@Column
	val OffsetTimeOriginal: String?,

	@Column
	val OffsetTimeDigitized: String?,

	@Column(nullable = false)
	val raw: String,

	@Column(nullable = false)
	val date: LocalDateTime,
) {
	companion object {
		private val mapper = jacksonObjectMapper()

		fun from(photoId: String, node: JsonNode): PhotoMetadata {
			return PhotoMetadata(
				photoId = photoId,
				SubSecDateTimeOriginal = node["SubSecDateTimeOriginal"]?.textValue(),
				DateTimeOriginal = node["DateTimeOriginal"]?.textValue(),
				GPSDateTime = node["GPSDateTime"]?.textValue(),
				TimeStamp = node["TimeStamp"]?.textValue(),
				ModifyDate = node["ModifyDate"]?.textValue(),
				CreationDate = node["CreationDate"]?.textValue(),
				OffsetTime = node["OffsetTime"]?.textValue(),
				OffsetTimeOriginal = node["OffsetTimeOriginal"]?.textValue(),
				OffsetTimeDigitized = node["OffsetTimeDigitized"]?.textValue(),
				raw = mapper.writeValueAsString(node),
				date = LocalDateTime.now(),
			)
		}
	}
}
