package kim.hyunsub.video.repository.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_category")
data class VideoCategory(
	@Id
	val id: Int,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val displayName: String,

	@Column(nullable = false)
	val iconHtmlClass: String,

	@Column(nullable = false)
	val listHtmlClass: String,

	@Column(nullable = false)
	val itemCss: String,

	@Column(nullable = false)
	val authority: String,
)
