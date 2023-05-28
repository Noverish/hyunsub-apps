package kim.hyunsub.auth.service

import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SignOutService(
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
) {
	private val log = KotlinLogging.logger { }

	fun signOut(idNo: String) {
		log.info("[SignOut] idNo={}", idNo)

		val user = userRepository.findByIdOrNull(idNo)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_USER)
		log.info("[SignOut] user={}", user)

		val authorities = userAuthorityRepository.findByUserIdNo(idNo)
		log.info("[SignOut] authorities={}", authorities)

		userAuthorityRepository.deleteAll(authorities)
		userRepository.delete(user)
	}
}
