package kim.hyunsub.friend.model.api

import kim.hyunsub.friend.repository.entity.Friend

fun Friend.toApiPreview() = ApiFriendPreview(
	id = id,
	name = name,
)
