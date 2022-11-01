package kim.hyunsub.division.repository.entity

import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.division.repository.RecordShareRepository
import org.springframework.data.repository.findByIdOrNull
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "division_record_share")
data class RecordShare(
	@Id
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false, columnDefinition = "CHAR(6)")
	val recordId: String,

	@Column(nullable = false, columnDefinition = "CHAR(5)")
	val userId: String,

	@Column(nullable = false)
	val realAmount: Int,

	@Column(nullable = false)
	val shouldAmount: Int,
) {
	companion object {
		fun generateId(repository: RecordShareRepository, generator: RandomGenerator): String {
			for (i in 0 until 3) {
				val newId = generator.generateRandomString(6)
				if (repository.findByIdOrNull(newId) == null) {
					return newId
				}
			}
			throw RuntimeException("Failed to generate new Video id")
		}
	}
}
