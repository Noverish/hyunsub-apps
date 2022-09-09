package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_subtitle")
data class VideoSubtitle(
	@Id
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false)
	val path: String,

	@Column(nullable = false, columnDefinition = "CHAR(6)")
	val videoId: String,
)
