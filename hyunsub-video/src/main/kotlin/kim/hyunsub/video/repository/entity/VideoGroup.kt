package kim.hyunsub.video.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_group")
data class VideoGroup(
	@Id
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false)
	val name: String,
)
