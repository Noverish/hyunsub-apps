package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.repository.entity.DutchMember

data class ApiDutchMember(
	val id: String,
	val name: String,
)

fun DutchMember.toApi() = ApiDutchMember(
	id = id,
	name = name,
)
