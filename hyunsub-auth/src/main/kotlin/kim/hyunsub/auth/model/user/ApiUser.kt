package kim.hyunsub.auth.model.user

import kim.hyunsub.auth.repository.entity.User

data class ApiUser(
	val idNo: String,
	val username: String,
	val authorities: List<Int>,
)

fun User.toDto(authorities: List<Int> = emptyList()) = ApiUser(
	idNo = idNo,
	username = username,
	authorities = authorities,
)
