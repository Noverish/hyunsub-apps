package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kim.hyunsub.video.model.api.ApiVideoCategory

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

	@Column(nullable = false)
	val path: String,
) {
	fun toDto() = ApiVideoCategory(this)
}
