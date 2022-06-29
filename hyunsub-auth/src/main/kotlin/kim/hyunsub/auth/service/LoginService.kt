package kim.hyunsub.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.*
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.util.log.Log
import org.springframework.stereotype.Service

@Service
class LoginService(
	private val userRepository: UserRepository,
	private val jwtService: JwtService,
	private val sessionService: LoginFailureSessionService,
	private val captchaService: CaptchaService,
	private val authorityService: AuthorityService,
) {
	companion object : Log

	/**
	 * @return JWT
	 */
	fun login(params: LoginParams): LoginResult {
		val session = sessionService.getSession(params.remoteAddr) ?: LoginFailureSession(params.remoteAddr)
		try {
			return loginInternal(params, session)
		} catch (e: ErrorCodeException) {
			session.failCnt += 1
			sessionService.putSession(params.remoteAddr, session)
			val payload = LoginApiError(session.needCaptcha)
			throw ErrorCodeException(e.errorCode, payload)
		}
	}

	private fun loginInternal(params: LoginParams, session: LoginFailureSession): LoginResult {
		if (session.needCaptcha) {
			val captcha = params.captcha ?: throw ErrorCodeException(ErrorCode.CAPTCHA_REQUIRED)
			val captchaSuccess = captchaService.verify(captcha, params.remoteAddr)
			if (!captchaSuccess) {
				throw ErrorCodeException(ErrorCode.CAPTCHA_FAILURE)
			}
		}

		val user = userRepository.findByUsername(params.username) ?: throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)

		val hashedPw = user.password
		val correct = BCrypt.verifyer().verify(params.password.toCharArray(), hashedPw).verified
		if (!correct) {
			throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)
		}

		val allowedPaths = authorityService.getAllowedPaths(user.idNo)

		val payload = JwtPayload(user.idNo, allowedPaths)
		val jwt = jwtService.issue(payload)
		return LoginResult(
			idNo = user.idNo,
			jwt = jwt,
		)
	}
}
