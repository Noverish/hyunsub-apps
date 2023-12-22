package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "video_entry")
data class VideoEntry(
	@Id
	val id: String,

	@Column(nullable = false)
	val name: String,

	@Column
	var thumbnail: String?,

	@Column(nullable = false)
	val category: String,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column
	val videoGroupId: String? = null,
) {
	companion object {
		private val yearRegex = Regex(" \\(\\d{4}\\)")

		fun parseTitle(name: String) = name.replace(yearRegex, "")
	}
}
