package kim.hyunsub.friend.repository

import kim.hyunsub.friend.repository.entity.FriendMeet
import kim.hyunsub.friend.repository.entity.FriendMeetId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

interface FriendMeetRepository : JpaRepository<FriendMeet, FriendMeetId> {
	fun findByFriendId(friendId: String): List<FriendMeet>
}

fun FriendMeetRepository.findByIdOrNull(friendId: String, date: LocalDate) =
	findByIdOrNull(FriendMeetId(friendId, date))
