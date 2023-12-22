package kim.hyunsub.video.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

@Entity
@Table(name = "video")
data class Video(
	@Id
	val id: String,

	@Column(nullable = false)
	var path: String,

	@Column
	var thumbnail: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column(nullable = false)
	val entryId: String,

	@Column
	val season: String? = null,
) {
	companion object {
		fun parseTitle(path: String) = Path(path).nameWithoutExtension
	}
}
