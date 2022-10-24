package kim.hyunsub.division.repository.entity

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

	@Column(nullable = false, columnDefinition = "CHAR(3)")
	val currencyCode: String,

	@Column(nullable = false)
	val realAmount: Int,

	@Column(nullable = false)
	val shouldAmount: Int,
)
