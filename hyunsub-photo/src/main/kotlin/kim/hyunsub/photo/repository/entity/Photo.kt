package kim.hyunsub.photo.repository.entity

import java.time.LocalDateTime
import javax.persistence.*

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
)
