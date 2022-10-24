package kim.hyunsub.division.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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

	@Column(nullable = false)
	val date: LocalDateTime,

	@Column(nullable = false, columnDefinition = "CHAR(6)")
	val gatheringId: String,
)
