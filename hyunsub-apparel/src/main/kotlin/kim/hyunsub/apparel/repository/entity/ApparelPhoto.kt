package kim.hyunsub.apparel.repository.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "apparel_photo")
data class ApparelPhoto(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,

	@Column(nullable = false)
	val apparelId: String,

	@Column(nullable = false)
	val fileName: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now(),
)
