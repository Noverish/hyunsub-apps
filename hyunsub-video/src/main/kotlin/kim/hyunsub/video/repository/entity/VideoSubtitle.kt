package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "video_subtitle")
data class VideoSubtitle(
	@Id
	val id: String,

	@Column(nullable = false)
	var path: String,

	@Column(nullable = false)
	val videoId: String,
)
