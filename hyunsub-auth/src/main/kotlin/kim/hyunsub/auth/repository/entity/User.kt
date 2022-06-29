package kim.hyunsub.auth.repository.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user")
data class User(
	@Id
	@Column(columnDefinition = "CHAR(5)")
	val idNo: String,

	@Column(nullable = false)
	val username: String,

	@Column(nullable = false)
	val password: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
