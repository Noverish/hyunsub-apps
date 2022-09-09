package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_metadata")
data class VideoMetadata(
	@Id
	val path: String,

	@Column(nullable = false)
	val nbStreams: Int,

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

	@Column(nullable = false)
	val videoCodecName: String,

	@Column(nullable = false)
	val videoProfile: String,

	@Column(nullable = false)
	val videoPixFmt: String,

	@Column(nullable = false)
	val videoLevel: Int,

	@Column(nullable = false)
	val videoBitrate: Int,

	@Column(nullable = false)
	val audioCodecName: String,

	@Column(nullable = false)
	val audioProfile: String,

	@Column(nullable = false)
	val audioSampleRate: Int,

	@Column(nullable = false)
	val audioChannelLayout: String,

	@Column(nullable = false)
	val audioBitrate: Int,

	@Column(nullable = false)
	val date: LocalDateTime,
)
