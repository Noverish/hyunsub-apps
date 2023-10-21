package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
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
