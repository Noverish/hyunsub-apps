package kim.hyunsub.auth.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kim.hyunsub.auth.config.SessionConfiguration
import kim.hyunsub.auth.model.LoginFailureSession
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("session_test")
@SpringBootTest(classes = [RedisAutoConfiguration::class, SessionConfiguration::class, LoginFailureSessionService::class])
class LoginFailureSessionServiceTest(val service: LoginFailureSessionService) : FreeSpec({
	val remoteAddr = "127.0.0.1"

	"loginFailureSession" {
		val session = LoginFailureSession(remoteAddr)
		val sessionKey = service.createSession(session)
		val session2 = service.getSession(sessionKey)
		session2 shouldBe session

		session.failCnt = 1
		service.putSession(sessionKey, session)
		val session3 = service.getSession(sessionKey)
		session3 shouldBe session

		session.failCnt = 2
		service.putSessionWithoutExtend(sessionKey, session)
		val session4 = service.getSession(sessionKey)
		session4 shouldBe session

		service.deleteSession(sessionKey)
		val session5 = service.getSession(sessionKey)
		session5 shouldBe null
	}
})
