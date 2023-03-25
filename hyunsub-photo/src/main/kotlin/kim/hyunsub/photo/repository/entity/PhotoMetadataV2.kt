package kim.hyunsub.photo.repository.entity

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo_metadata2")
data class PhotoMetadataV2(
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

	@Type(type = "text")
	@Column(nullable = false)
	val raw: String,

	@Column(nullable = false)
	val date: LocalDateTime,
) {
	companion object {
		private val mapper = jacksonObjectMapper()

		fun from(photoId: String, node: JsonNode): PhotoMetadataV2 {
			return PhotoMetadataV2(
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
