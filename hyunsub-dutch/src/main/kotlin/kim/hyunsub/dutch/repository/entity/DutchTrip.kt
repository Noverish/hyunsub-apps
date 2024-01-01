package kim.hyunsub.dutch.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import kim.hyunsub.dutch.model.DutchCurrency
import java.time.LocalDateTime

@Entity
@Table(name = "dutch_trip")
data class DutchTrip(
	@Id
	val id: String,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	val currency: DutchCurrency,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
