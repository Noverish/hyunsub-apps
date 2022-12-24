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
			paths = flatMapPaths(idNo, authorities.mapNotNull { it.path }),
			uploads = flatMapPaths(idNo, authorities.mapNotNull { it.upload }),
			apis = flatMapPaths(idNo, authorities.mapNotNull { it.api }),
		)
	}

	private fun flatMapPaths(idNo: String, list: List<String>): List<String> {
		return list.flatMap { it.split(',') }
			.map { it.replace("{idNo}", idNo) }
			.distinct()
	}
}
