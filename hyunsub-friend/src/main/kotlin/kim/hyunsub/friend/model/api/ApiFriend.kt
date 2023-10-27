package kim.hyunsub.friend.model.api

import kim.hyunsub.friend.repository.entity.Friend

data class ApiFriend(
	val id: String,
	val name: String,
	val birthday: String?,
	val tags: List<String>,
	val description: String?,
)

fun Friend.toApi(tags: List<String>) = ApiFriend(
	id = id,
	name = name,
	birthday = birthday,
	tags = tags,
	description = description,
)