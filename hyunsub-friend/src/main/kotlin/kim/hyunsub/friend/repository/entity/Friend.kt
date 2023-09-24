package kim.hyunsub.friend.repository.entity

import org.hibernate.annotations.Type
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
	val name: String,

	@Column
	val birthday: String?,

	@Column(nullable = false)
	val tags: String,

	@Type(type = "text")
	@Column(nullable = false)
	val description: String,
)
