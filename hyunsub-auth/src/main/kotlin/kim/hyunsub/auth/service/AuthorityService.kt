package kim.hyunsub.auth.service

import kim.hyunsub.auth.model.UserAuthoritySearchResult
import kim.hyunsub.auth.repository.AuthorityRepository
import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.stereotype.Service

@Service
class AuthorityService(
	private val userAuthorityRepository: UserAuthorityRepository,
	private val authorityRepository: AuthorityRepository,
) {
	fun searchAuthorities(idNo: String): UserAuthoritySearchResult {
		val authorities = userAuthorityRepository.findByUserIdNo(idNo)
			.map { it.authorityId }
			.let { authorityRepository.findAllById(it) }

		val authorityNames = authorities.map { it.name }
		val authorityPaths = authorities.flatMap { it.paths.split(",") }
		return UserAuthoritySearchResult(authorityNames, authorityPaths)
	}

	fun getUserAuth(idNo: String): UserAuth {
		val searchResult = searchAuthorities(idNo)
		return UserAuth(
			idNo = idNo,
			authorityNames = searchResult.names,
			authorityPaths = searchResult.paths,
		)
	}
}
