package kim.hyunsub.photo.repository.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo")
data class Photo(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,

	@Column(nullable = false)
	val path: String,

	@Column(nullable = false)
	val width: Int,

	@Column(nullable = false)
	val height: Int,

	@Column(nullable = false)
	val date: LocalDateTime,

	@Column(nullable = false)
	val size: Int,

	@Column
	val albumId: Int?,

	@Column(nullable = false)
	val regDt: LocalDateTime,
) {
	fun update(metadata: PhotoMetadata): Photo {
		val mapper = jacksonObjectMapper()
		val node = mapper.readTree(metadata.data)
		return this.copy(
			width = node[0]["ImageWidth"].asInt(),
			height = node[0]["ImageHeight"].asInt(),
			size = node[0]["FileSize"].asInt(),
		)
	}
}
