package kim.hyunsub.apparel.repository.entity

import kim.hyunsub.common.random.RandomGenerator
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

	@Column
	val itemNo: String?,

	@Column(nullable = false)
	val name: String,

	@Column
	val brand: String?,

	@Column(nullable = false)
	val type: String,

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

	@Column(columnDefinition = "CHAR(10)")
	val imageId: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
) {
	companion object {
		fun generateId(generator: RandomGenerator) = generator.generateRandomId(8)
	}
}
