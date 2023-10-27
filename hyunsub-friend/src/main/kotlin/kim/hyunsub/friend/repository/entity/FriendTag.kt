package kim.hyunsub.friend.repository.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "friend_tag")
@IdClass(FriendTagId::class)
data class FriendTag(
	@Id
	val friendId: String,

	@Id
	val tag: String,

	@Column(nullable = false)
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)

data class FriendTagId(
	val friendId: String = "",
	val tag: String = "",
) : Serializable
