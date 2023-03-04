package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_rename_history")
data class VideoRenameHistory(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,

	@Column(nullable = false)
	val videoId: String,

	@Column(nullable = false)
	val from: String,

	@Column(nullable = false)
	val to: String,

	@Column(nullable = false)
	val isRegex: Boolean,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
)
