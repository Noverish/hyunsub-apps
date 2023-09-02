package kim.hyunsub.auth.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.model.LoginApiError
import kim.hyunsub.auth.model.LoginFailureSession
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.model.UserLanguage
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException

class LoginServiceTest : FreeSpec({
	val userRepository = mockk<UserRepository>()
	val sessionService = mockk<LoginFailureSessionService>()
	val captchaService = mockk<CaptchaService>()
	val tokenService = mockk<TokenService>()
	val service = LoginService(userRepository, sessionService, captchaService, tokenService)

	val user = mockk<User>(relaxed = true)
	val params = mockk<LoginParams>(relaxed = true)
	val loginFailureSession = mockk<LoginFailureSession>(relaxed = true)

	val idNo = "kotest_id_no"
	val username = "kotest_username"
	val password = "kotest_password"
	val hashed = "\$2a\$12\$umWkbsinybLfpuEknH3IqufPEtv8hbUYJts6GN/fBAZZA.fIpp1aK"
	val token = "kotest_token"
	val remoteAddr = "kotest_remote_addr"
	val lang = UserLanguage.KO

	beforeTest {
		every { user.idNo } returns idNo
		every { user.password } returns hashed
		every { user.lang } returns lang
		every { params.username } returns username
		every { params.password } returns password
		every { params.remoteAddr } returns remoteAddr

		every { userRepository.findByUsername(username) } returns user
		every { loginFailureSession.failCnt } returns 0
		every { sessionService.getSession(remoteAddr) } returns loginFailureSession
		every { sessionService.putSession(eq(remoteAddr), any()) } returns Unit
		every { tokenService.issue(user) } returns token
	}

	"Success" {
		val result = service.login(params)
		result shouldBe LoginResult(idNo, token, lang)
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
		every { sessionService.getSession(remoteAddr) } returns session
		every { userRepository.findByUsername(username) } returns null

		for (i in 1..AuthConstants.FAIL_NUM_TO_CAPTCHA) {
			val ex = shouldThrow<ErrorCodeException> { service.login(params) }
			ex.errorCode shouldBe ErrorCode.NOT_EXIST_USER
			(ex.payload as? LoginApiError)?.needCaptcha shouldBe (i == AuthConstants.FAIL_NUM_TO_CAPTCHA)
		}
	}
})
