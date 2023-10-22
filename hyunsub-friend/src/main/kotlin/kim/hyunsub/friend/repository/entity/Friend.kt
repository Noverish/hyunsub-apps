package kim.hyunsub.friend.repository.entity

import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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

	@Column(nullable = false)
	var tags: String,

	@Type(type = "text")
	@Column
	var description: String?,

	@Column(nullable = true)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
