package kim.hyunsub.apparel.repository.entity

import kim.hyunsub.common.random.RandomGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "apparel_image")
data class ApparelImage(
	@Id
	@Column(columnDefinition = "CHAR(10)")
	val id: String,

	@Column(nullable = false, columnDefinition = "CHAR(8)")
	val apparelId: String,

	@Column(nullable = false)
	val ext: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
) {
	companion object {
		fun generateId(generator: RandomGenerator) = generator.generateRandomId(10)
	}

	val fileName: String
		get() = "$id.$ext"
}
