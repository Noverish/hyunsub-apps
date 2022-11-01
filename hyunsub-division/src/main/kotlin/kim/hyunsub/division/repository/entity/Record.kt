package kim.hyunsub.division.repository.entity

import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.division.model.CurrencyCode
import kim.hyunsub.division.repository.RecordRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "division_record")
data class Record(
	@Id
	@Column(columnDefinition = "CHAR(6)")
	val id: String,

	@Column(nullable = false)
	val content: String,

	@Column(nullable = false)
	val location: String,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(3)")
	val currency: CurrencyCode,

	@Column(nullable = false)
	val date: LocalDateTime,

	@Column(nullable = false, columnDefinition = "CHAR(6)")
	val gatheringId: String,
) {
	companion object {
		fun generateId(repository: RecordRepository, generator: RandomGenerator): String {
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
