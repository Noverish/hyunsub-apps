package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "video_group")
data class VideoGroup(
	@Id
	val id: String,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val categoryId: Int,
)
