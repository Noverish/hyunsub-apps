package kim.hyunsub.apparel.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "apparel")
data class Apparel(
	@Id
	@Column(length = 8)
	val id: String,

	@Column(nullable = false)
	val userId: String,

	@Column
	val itemNo: String?,

	@Column(nullable = false)
	val name: String,

	@Column
	val brand: String?,

	@Column(nullable = false)
	val category: String,

	@Column
	val size: String?,

	@Column
	val color: String?,

	@Column
	val originPrice: Int?,

	@Column
	val discountPrice: Int?,

	@Column
	val material: String?,

	@Column
	val size2: String?,

	@Column
	val buyDt: LocalDate?,

	@Column
	val buyLoc: String?,

	@Column
	val makeDt: String?,

	@Column(length = 10)
	val imageId: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime,

	@Column
	val discardDt: LocalDateTime?,
)
