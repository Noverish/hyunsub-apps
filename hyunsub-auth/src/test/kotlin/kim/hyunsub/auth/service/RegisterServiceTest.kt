package kim.hyunsub.auth.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException

class RegisterServiceTest: FreeSpec({
	val userRepository = mockk<UserRepository>()
	val captchaService = mockk<CaptchaService>()
	val randomGenerator = mockk<RandomGenerator>()
	val service = RegisterService(userRepository, captchaService, randomGenerator)

	val slot = slot<User>()
	val params = mockk<RegisterParams>()
	val username = "kotest_username"
	val password = "kotest_password"
	val captcha = "kotest_captcha"
	val remoteAddr = "1.2.3.4"
	val idNo = "12345678"

	beforeTest {
		every { params.username } returns username
		every { params.password } returns password
		every { params.captcha } returns captcha
		every { params.remoteAddr } returns remoteAddr
		every { captchaService.verify(captcha, remoteAddr) } returns true
		every { userRepository.findByUsername(username) } returns null
		every { userRepository.existsById(any()) } returns false
		every { userRepository.saveAndFlush(capture(slot)) } answers { slot.captured }
		every { randomGenerator.generateRandomString(any()) } returns idNo
	}

	"Success" {
		val result = service.register(params)
		result.idNo shouldNotBe null
	}

	"Captcha Failed" {
		every { captchaService.verify(captcha, remoteAddr) } returns false
		val ex = shouldThrow<ErrorCodeException> { service.register(params) }
		ex.errorCode shouldBe ErrorCode.CAPTCHA_FAILURE
	}

	"Already exist username" {
		every { userRepository.findByUsername(username) } returns mockk()
		val ex = shouldThrow<ErrorCodeException> { service.register(params) }
		ex.errorCode shouldBe ErrorCode.ALREADY_EXIST_USERNAME
	}
})
