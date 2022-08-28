package kim.hyunsub.auth.service

import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SignOutService(
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
) {
	companion object : Log

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
