package kim.hyunsub.friend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "friend")
data class Friend(
	@Id
	val id: String,

	@Column(nullable = false)
	val fromUserId: String,

	@Column
	val toUserId: String?,

	@Column(nullable = false)
	var name: String,

	@Column
	var birthday: String?,

	@Column
	var description: String?,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
