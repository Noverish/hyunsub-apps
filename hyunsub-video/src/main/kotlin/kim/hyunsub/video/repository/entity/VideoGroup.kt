package kim.hyunsub.video.repository.entity

import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.video.repository.VideoGroupRepository
import org.springframework.data.repository.findByIdOrNull
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

	@Column(nullable = false)
	val categoryId: Int,
) {
	companion object {
		fun generateId(repository: VideoGroupRepository, generator: RandomGenerator): String {
			for (i in 0 until 3) {
				val newId = generator.generateRandomId(6)
				if (repository.findByIdOrNull(newId) == null) {
					return newId
				}
			}
			throw RuntimeException("Failed to generate new VideoGroup id")
		}
	}
}
