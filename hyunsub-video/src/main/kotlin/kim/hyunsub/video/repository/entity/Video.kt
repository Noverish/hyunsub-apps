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
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false)
	val path: String,

	@Column
	val thumbnail: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column(columnDefinition = "CHAR(6)")
	val videoEntryId: String,

	@Column
	val videoSeason: String? = null,
)
