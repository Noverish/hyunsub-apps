package kim.hyunsub.video.repository.entity

import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video")
data class Video(
	@Id
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false)
	val path: String,

	@Column
	val thumbnail: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column(columnDefinition = "CHAR(6)")
	val videoEntryId: String,

	@Column
	val videoSeason: String? = null,
) {
	companion object {
		fun generateId(repository: VideoRepository, generator: RandomGenerator): String {
			for (i in 0 until 3) {
				val newId = generator.generateRandomId(6)
				if (repository.findByIdOrNull(newId) == null) {
					return newId
				}
			}
			throw RuntimeException("Failed to generate new Video id")
		}
	}
}
