package kim.hyunsub.video.repository.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

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
