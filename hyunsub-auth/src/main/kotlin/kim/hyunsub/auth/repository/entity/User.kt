package kim.hyunsub.auth.repository.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
	@Id
	val idNo: String,

	@Column(nullable = false)
	val username: String,

	@Column(nullable = false)
	val password: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
