package kim.hyunsub.friend.repository

import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.friend.repository.entity.FriendMeet
import kim.hyunsub.friend.repository.entity.FriendMeetId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

interface FriendMeetRepository : JpaRepository<FriendMeet, FriendMeetId> {
	fun findByFriendId(friendId: String): List<FriendMeet>

	@Query(
		"""
			SELECT new kim.hyunsub.friend.model.api.ApiFriendPreview(b.id, b.name)
			FROM FriendMeet a
			INNER JOIN Friend b ON b.id = a.friendId
			WHERE a.date = :date
		"""
	)
	fun findByDate(date: LocalDate): List<ApiFriendPreview>
}

fun FriendMeetRepository.findByIdOrNull(friendId: String, date: LocalDate) =
	findByIdOrNull(FriendMeetId(friendId, date))
