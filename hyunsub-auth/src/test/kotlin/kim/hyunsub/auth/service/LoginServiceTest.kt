package kim.hyunsub.auth.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kim.hyunsub.auth.config.AppConstants
import kim.hyunsub.auth.model.LoginApiError
import kim.hyunsub.auth.model.LoginFailureSession
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException

class LoginServiceTest : FreeSpec({
	val userRepository = mockk<UserRepository>()
	val jwtService = mockk<JwtService>()
	val loginFailureSessionService = mockk<LoginFailureSessionService>()
	val captchaService = mockk<CaptchaService>()
	val authorityService = mockk<AuthorityService>()
	val service = LoginService(userRepository, jwtService, loginFailureSessionService, captchaService, authorityService)

	val user = mockk<User>(relaxed = true)
	val params = mockk<LoginParams>(relaxed = true)
	val loginFailureSession = mockk<LoginFailureSession>(relaxed = true)

	val idNo = "kotest_id_no"
	val username = "kotest_username"
	val password = "kotest_password"
	val hashed = "\$2a\$12\$umWkbsinybLfpuEknH3IqufPEtv8hbUYJts6GN/fBAZZA.fIpp1aK"
	val jwt = "kotest_jwt"
	val remoteAddr = "kotest_remote_addr"
	val allowedPaths = listOf("/a", "/b")

	beforeTest {
		every { user.idNo } returns idNo
		every { user.password } returns hashed
		every { params.username } returns username
		every { params.password } returns password
		every { params.remoteAddr } returns remoteAddr

		every { userRepository.findByUsername(username) } returns user
		every { jwtService.issue(any()) } returns jwt
		every { loginFailureSession.failCnt } returns 0
		every { loginFailureSessionService.getSession(remoteAddr) } returns loginFailureSession
		every { loginFailureSessionService.putSession(eq(remoteAddr), any()) } returns Unit
		every { authorityService.getAllowedPaths(idNo) } returns allowedPaths
	}

	"Success" {
		val result = service.login(params)
		result shouldBe LoginResult(idNo, jwt)
	}

	"Not exist user" {
		every { userRepository.findByUsername(username) } returns null
		val ex = shouldThrow<ErrorCodeException> { service.login(params) }
		ex.errorCode shouldBe ErrorCode.NOT_EXIST_USER
	}

	"Incorrect password" {
		every { user.password } returns "wrong_hashed"
		val ex = shouldThrow<ErrorCodeException> { service.login(params) }
		ex.errorCode shouldBe ErrorCode.NOT_EXIST_USER
	}

	"Captcha Scenario" {
		val session = LoginFailureSession(remoteAddr)
		every { loginFailureSessionService.getSession(remoteAddr) } returns session
		every { userRepository.findByUsername(username) } returns null

		for (i in 1.. AppConstants.FAIL_NUM_TO_CAPTCHA) {
			val ex = shouldThrow<ErrorCodeException> { service.login(params) }
			ex.errorCode shouldBe ErrorCode.NOT_EXIST_USER
			(ex.payload as? LoginApiError)?.needCaptcha shouldBe (i == AppConstants.FAIL_NUM_TO_CAPTCHA)
		}
	}
})
