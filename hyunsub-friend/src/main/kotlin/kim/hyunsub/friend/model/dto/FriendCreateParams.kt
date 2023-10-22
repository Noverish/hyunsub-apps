package kim.hyunsub.friend.model.dto

data class FriendCreateParams(
	val userId: String?,
	val name: String,
	val birthday: String?,
	val tags: List<String>,
	val description: String?,
)
