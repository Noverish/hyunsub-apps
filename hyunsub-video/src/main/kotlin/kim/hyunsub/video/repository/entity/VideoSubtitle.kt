package kim.hyunsub.video.repository.entity

import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.video.repository.VideoSubtitleRepository
import org.springframework.data.repository.findByIdOrNull
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
	val path: String,

	@Column(nullable = false)
	val videoId: String,
) {
	companion object {
		fun generateId(repository: VideoSubtitleRepository, generator: RandomGenerator): String {
			for (i in 0 until 3) {
				val newId = generator.generateRandomId(6)
				if (repository.findByIdOrNull(newId) == null) {
					return newId
				}
			}
			throw RuntimeException("Failed to generate new Video Subtitle id")
		}
	}
}
