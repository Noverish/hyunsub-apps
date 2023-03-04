package kim.hyunsub.video.repository.entity

import kim.hyunsub.video.model.api.RestApiVideoEntry
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video_entry")
data class VideoEntry(
	@Id
	val id: String,

	@Column(nullable = false)
	val name: String,

	@Column
	val thumbnail: String?,

	@Column(nullable = false)
	val category: String,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column
	val videoGroupId: String? = null,
) {
	fun toDto() = RestApiVideoEntry(this)
}
