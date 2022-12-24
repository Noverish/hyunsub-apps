package kim.hyunsub.video.repository.entity

import kim.hyunsub.video.model.VideoRenameParams
import java.time.LocalDateTime
import javax.persistence.*

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

	@Column(nullable = false, columnDefinition = "TINYINT")
	val isRegex: Boolean,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
) {
	constructor(params: VideoRenameParams) : this(
		videoId = params.videoId,
		from = params.from,
		to = params.to,
		isRegex = params.isRegex
	)
}
