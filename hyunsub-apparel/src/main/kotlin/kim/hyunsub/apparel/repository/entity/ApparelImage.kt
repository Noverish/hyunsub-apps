package kim.hyunsub.apparel.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
