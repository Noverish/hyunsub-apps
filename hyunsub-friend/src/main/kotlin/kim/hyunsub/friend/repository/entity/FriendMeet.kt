package kim.hyunsub.friend.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDate

@Entity
@Table(name = "friend_meet")
@IdClass(FriendMeetId::class)
data class FriendMeet(
	@Id
	val userId: String,

	@Id
	val friendId: String,

	@Id
	val date: LocalDate,
)

data class FriendMeetId(
	val userId: String = "",
	val friendId: String = "",
	val date: LocalDate = LocalDate.now(),
) : Serializable
