package kim.hyunsub.friend.model.api

import java.time.LocalDate

data class ApiMeetFriendSearchResult(
	val date: LocalDate,
	val friends: List<ApiFriendPreview>,
)
