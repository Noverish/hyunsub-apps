package kim.hyunsub.apparel.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "apparel_image")
data class ApparelImage(
	@Id
	@Column(length = 10)
	val id: String,

	@Column(nullable = false, length = 8)
	val apparelId: String,

	@Column(nullable = false)
	val ext: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
) {
	val fileName: String
		get() = "$id.$ext"
}
