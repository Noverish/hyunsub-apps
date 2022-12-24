package kim.hyunsub.auth.service

import kim.hyunsub.auth.repository.AuthorityRepository
import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.stereotype.Service

@Service
class AuthorityService(
	private val userAuthorityRepository: UserAuthorityRepository,
	private val authorityRepository: AuthorityRepository,
) {
	fun getUserAuth(idNo: String): UserAuth {
		val authorities = userAuthorityRepository.findByUserIdNo(idNo)
			.map { it.authorityId }
			.let { authorityRepository.findAllById(it) }

		return UserAuth(
			idNo = idNo,
			names = authorities.map { it.name },
			paths = authorities.flatMap { it.path?.split(",") ?: emptyList() },
			uploads = authorities.flatMap { it.upload?.split(",") ?: emptyList() },
			apis = authorities.flatMap { it.api?.split(",") ?: emptyList() },
		)
	}
}
