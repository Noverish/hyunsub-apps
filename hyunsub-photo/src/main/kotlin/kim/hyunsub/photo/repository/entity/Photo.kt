package kim.hyunsub.photo.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo")
data class Photo(
	@Id
	val id: Int,

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
)
