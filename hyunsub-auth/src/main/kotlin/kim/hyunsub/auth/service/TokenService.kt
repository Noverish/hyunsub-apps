package kim.hyunsub.auth.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import kim.hyunsub.auth.config.TokenProperties
import kim.hyunsub.auth.model.TokenPayload
import kim.hyunsub.auth.repository.entity.User
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class TokenService(
	private val tokenProperties: TokenProperties,
	private val userAuthService: UserAuthService,
) {
	companion object {
		const val PAYLOAD_FIELD = "payload"
	}

	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()
	private val privateKey = CryptEccUtil.retrievePrivateKey(tokenProperties.private)
	private val publicKey = CryptEccUtil.retrievePublicKey(tokenProperties.public)

	fun issue(user: User, duration: Duration? = null): String {
		log.debug { "[Token Issue] user=$user" }
		val expire = Date(System.currentTimeMillis() + (duration ?: tokenProperties.duration).toMillis())

		val payload = TokenPayload(
			idNo = user.idNo,
			username = user.username,
			authorities = userAuthService.getAuthorities(user.idNo)
		)

		return Jwts.builder()
			.setExpiration(expire)
			.claim(PAYLOAD_FIELD, mapper.writeValueAsString(payload))
			.signWith(privateKey, SignatureAlgorithm.ES512)
			.compact()
	}

	fun verify(token: String): TokenPayload {
		val json = Jwts.parserBuilder()
			.setSigningKey(publicKey)
			.build()
			.parseClaimsJws(token)
			.body
			.get(PAYLOAD_FIELD, String::class.java)

		return mapper.readValue(json)
	}
}
