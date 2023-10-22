package kim.hyunsub.friend.model.dto

data class FriendUpdateParams(
	val name: String,
	val birthday: String?,
	val tags: List<String>,
	val description: String?,
)
