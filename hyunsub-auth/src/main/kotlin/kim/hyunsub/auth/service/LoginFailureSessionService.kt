package kim.hyunsub.auth.service

import kim.hyunsub.auth.config.SessionTimeProperties
import kim.hyunsub.auth.model.LoginFailureSession
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Service

@Service
class LoginFailureSessionService(
	private val template: RedisTemplate<String, LoginFailureSession>,
	private val sessionTimeProperties: SessionTimeProperties,
	private val randomGenerator: RandomGenerator = RandomGenerator(),
) {
	companion object : Log

	fun createSession(session: LoginFailureSession): String {
		val sessionKey = randomGenerator.generateRandomString(15)
		val sessionTime = sessionTimeProperties.getSessionTime(LoginFailureSession::class.java)
		log.debug("Create Session: key={}, time={}, session={}", sessionKey, sessionTime, session)
		template.opsForValue().set(sessionKey, session, sessionTime)
		return sessionKey
	}

	fun putSession(sessionKey: String, session: LoginFailureSession) {
		val sessionTime = sessionTimeProperties.getSessionTime(LoginFailureSession::class.java)
		log.debug("Put Session: key={}, time={}, session={}", sessionKey, sessionTime, session)
		template.opsForValue().set(sessionKey, session, sessionTime)
	}

	fun putSessionWithoutExtend(sessionKey: String, session: LoginFailureSession) {
		log.debug("Put Session Without Extend: key={}, session={}", sessionKey, session)
		val script = RedisScript.of<LoginFailureSession>("return redis.call('SET', KEYS[1], ARGV[1], 'KEEPTTL')")
		template.execute(script, listOf(sessionKey), session)
	}

	fun getSession(sessionKey: String): LoginFailureSession? {
		return template.opsForValue().get(sessionKey).apply {
			log.debug("Get Session: key={}, session={}", sessionKey, this)
		}
	}

	fun deleteSession(sessionKey: String) {
		template.opsForValue().getAndDelete(sessionKey).apply {
			log.debug("Delete Session: key={}, session={}", sessionKey, this)
		}
	}
}
