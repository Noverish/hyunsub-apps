package kim.hyunsub.video.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_metadata")
data class VideoMetadata(
	@Id
	@Column
	val path: String,

	@Column(nullable = false)
	val duration: Int,

	@Column(nullable = false)
	val size: Long,

	@Column(nullable = false)
	val bitrate: Int,

	@Column(nullable = false)
	val width: Int,

	@Column(nullable = false)
	val height: Int,
)
