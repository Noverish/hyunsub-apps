package kim.hyunsub.photo.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo_metadata")
data class PhotoMetadata(
	@Id
	val path: String,

	@Column(nullable = false, columnDefinition = "TEXT")
	val data: String,

	@Column(nullable = false)
	val date: LocalDateTime,
)
