package kim.hyunsub.friend.model.dto

data class FriendSearchParams(
	val query: String,
	val page: Int = 0,
	val pageSize: Int = 10,
)
