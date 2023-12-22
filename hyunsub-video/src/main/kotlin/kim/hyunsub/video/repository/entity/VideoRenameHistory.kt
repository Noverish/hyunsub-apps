package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

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
