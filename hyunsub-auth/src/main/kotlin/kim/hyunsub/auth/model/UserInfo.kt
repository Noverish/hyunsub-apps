package kim.hyunsub.auth.model

import kim.hyunsub.auth.model.user.ApiUser
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.repository.entity.UserAuthority

data class UserInfo(
	val user: User,
	val authorities: List<UserAuthority>,
)

fun UserInfo.toApi() = ApiUser(
	idNo = user.idNo,
	username = user.username,
	authorities = authorities.map { it.authorityId },
)
