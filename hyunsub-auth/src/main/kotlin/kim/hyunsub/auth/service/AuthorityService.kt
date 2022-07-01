package kim.hyunsub.auth.service

import kim.hyunsub.auth.repository.AuthorityRepository
import kim.hyunsub.auth.repository.UserAuthorityRepository
import org.springframework.stereotype.Service

@Service
class AuthorityService(
	private val userAuthorityRepository: UserAuthorityRepository,
	private val authorityRepository: AuthorityRepository,
) {
	fun getAllowedPaths(idNo: String): List<String> =
		userAuthorityRepository.findByUserIdNo(idNo)
			.map { it.authorityId }
			.let { authorityRepository.findAllById(it) }
			.flatMap { it.paths.split(",") }
}
