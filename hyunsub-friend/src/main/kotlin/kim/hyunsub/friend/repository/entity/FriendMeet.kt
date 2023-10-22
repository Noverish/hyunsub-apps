package kim.hyunsub.friend.repository.entity

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "friend_meet")
@IdClass(FriendMeetId::class)
data class FriendMeet(
	@Id
	val friendId: String,

	@Id
	val date: LocalDate,
)

data class FriendMeetId(
	val friendId: String = "",
	val date: LocalDate = LocalDate.now(),
) : Serializable
