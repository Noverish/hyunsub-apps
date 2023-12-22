package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "video_history")
@IdClass(VideoHistoryId::class)
data class VideoHistory(
	@Id
	val userId: String,

	@Id
	val videoId: String,

	@Column(nullable = false)
	val time: Int,

	@Column(nullable = false)
	val date: LocalDateTime,
)

data class VideoHistoryId(
	val userId: String = "",
	val videoId: String = "",
) : Serializable
