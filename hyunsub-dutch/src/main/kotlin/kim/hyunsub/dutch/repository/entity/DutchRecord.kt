package kim.hyunsub.dutch.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "dutch_record")
data class DutchRecord(
	@Id
	val id: String,

	@Column(nullable = false)
	val content: String,

	@Column(nullable = false)
	val location: String,

	@Column(nullable = false)
	val currency: String,

	@Column(nullable = false)
	val date: LocalDateTime,

	@Column(nullable = false)
	val tripId: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
