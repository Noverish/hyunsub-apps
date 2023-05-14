package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video")
data class Video(
	@Id
	val id: String,

	@Column(nullable = false)
	val path: String,

	@Column
	val thumbnail: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column(nullable = false)
	val videoEntryId: String,

	@Column
	val videoSeason: String? = null,
)
