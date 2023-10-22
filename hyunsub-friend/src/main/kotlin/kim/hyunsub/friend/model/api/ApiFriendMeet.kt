package kim.hyunsub.friend.model.api

import kim.hyunsub.friend.repository.entity.FriendMeet
import java.time.LocalDate

data class ApiFriendMeet(
	val date: LocalDate,
)

fun FriendMeet.toApi() = ApiFriendMeet(
	date = date,
)
