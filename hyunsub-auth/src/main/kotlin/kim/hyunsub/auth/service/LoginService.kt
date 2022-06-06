package kim.hyunsub.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.ErrorCode
import kim.hyunsub.auth.model.JwtPayload
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginService(
	private val userRepository: UserRepository,
	private val jwtService: JwtService,
) {
	/**
	 * @return JWT
	 */
	fun login(params: LoginParams): String {
		val user = userRepository.findByUsername(params.username)
			?: throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)

		val hashedPw = user.password
		val correct = BCrypt.verifyer().verify(params.password.toCharArray(), hashedPw).verified
		if (!correct) {
			throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)
		}

		val payload = JwtPayload(user.idNo)
		return jwtService.issue(payload)
	}
}
