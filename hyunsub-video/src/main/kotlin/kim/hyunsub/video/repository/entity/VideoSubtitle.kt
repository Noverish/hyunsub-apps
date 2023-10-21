package kim.hyunsub.video.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
