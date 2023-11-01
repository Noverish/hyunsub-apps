package kim.hyunsub.friend.model.api

data class ApiFriendTagDetail(
	val name: String,
	val count: Long,
	val friends: List<ApiFriendPreview>,
)
