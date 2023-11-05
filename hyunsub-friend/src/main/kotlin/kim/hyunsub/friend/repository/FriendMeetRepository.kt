package kim.hyunsub.friend.repository

import kim.hyunsub.friend.repository.entity.FriendMeet
import kim.hyunsub.friend.repository.entity.FriendMeetId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

interface FriendMeetRepository : JpaRepository<FriendMeet, FriendMeetId> {
	@Query("SELECT a FROM FriendMeet a WHERE a.userId = :userId AND a.friendId = :friendId")
	fun select(userId: String, friendId: String): List<FriendMeet>

	@Query("DELETE FROM FriendMeet WHERE userId = :userId AND friendId = :friendId")
	fun delete(userId: String, friendId: String): Int
}

fun FriendMeetRepository.findByIdOrNull(userId: String, friendId: String, date: LocalDate) =
	findByIdOrNull(FriendMeetId(userId, friendId, date))
