package kim.hyunsub.friend.repository.entity

import java.time.LocalDate

interface FriendMeetAndName {
	val date: LocalDate
	val id: String
	val name: String
}
