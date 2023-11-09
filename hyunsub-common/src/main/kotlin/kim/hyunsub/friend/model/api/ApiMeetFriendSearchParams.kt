package kim.hyunsub.friend.model.api

import java.time.LocalDate

data class ApiMeetFriendSearchParams(
	val dates: List<LocalDate>,
)
