package kim.hyunsub.friend.model.api

import kim.hyunsub.friend.repository.entity.Friend
import java.time.LocalDate

data class ApiFriend(
	val id: String,
	val name: String,
	val birthday: String?,
	val tags: List<String>,
	val description: String?,
	val meets: List<LocalDate>,
)

fun Friend.toApi(tags: List<String>, meets: List<LocalDate>) = ApiFriend(
	id = id,
	name = name,
	birthday = birthday,
	tags = tags,
	description = description,
	meets = meets,
)
