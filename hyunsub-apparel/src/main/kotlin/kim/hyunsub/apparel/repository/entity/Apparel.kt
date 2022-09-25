package kim.hyunsub.apparel.repository.entity

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "apparel")
data class Apparel(
	@Id
	@Column(columnDefinition = "CHAR(8)")
	val id: String,

	@Column(nullable = false)
	val userId: String,

	@Column(nullable = false)
	val itemId: String,

	@Column(nullable = false)
	val name: String,

	@Column(nullable = false)
	val brand: String,

	@Column(nullable = false)
	val size: String,

	@Column(nullable = false)
	val color: String,

	@Column(nullable = false)
	val originPrice: Int,

	@Column
	val discountPrice: Int?,

	@Column(nullable = false)
	val buyDt: LocalDate,

	@Column(nullable = false)
	val buyLoc: String,

	@Column
	val makeDt: String?,

	@Column
	val thumbnail: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime,
)
