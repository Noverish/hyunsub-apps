package kim.hyunsub.auth.model.user

import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.repository.entity.UserAuthority

data class ApiUser(
	val idNo: String,
	val username: String,
	val authorities: List<Int>,
)

fun User.toApi(authorities: List<UserAuthority> = emptyList()) = ApiUser(
	idNo = idNo,
	username = username,
	authorities = authorities.map { it.authorityId },
)
