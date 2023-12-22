package kim.hyunsub.friend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "friend_tag")
@IdClass(FriendTagId::class)
data class FriendTag(
	@Id
	val userId: String,

	@Id
	val friendId: String,

	@Id
	val tag: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)

data class FriendTagId(
	val userId: String = "",
	val friendId: String = "",
	val tag: String = "",
) : Serializable
