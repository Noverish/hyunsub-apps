package kim.hyunsub.auth.service

import org.springframework.stereotype.Service
import java.time.Duration

@Service
class TokenGenerator(
	private val authorityService: AuthorityService,
	private val jwtService: JwtService,
) {
	fun generateToken(idNo: String, duration: Duration? = null): String {
		val userAuth = authorityService.getUserAuth(idNo)
		return jwtService.issue(userAuth, duration)
	}
}
