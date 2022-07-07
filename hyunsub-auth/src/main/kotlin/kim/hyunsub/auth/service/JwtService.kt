package kim.hyunsub.auth.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kim.hyunsub.auth.config.JwtProperties
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(private val jwtProperties: JwtProperties) {
	companion object {
		const val ALGORITHM = "AES/CBC/PKCS5Padding"
		const val CLAIM_NAME = "payload"
	}

	private val mapper = jacksonObjectMapper()
	private val jwtKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())
	private val aesKey = SecretKeySpec(Base64Utils.decodeFromString(jwtProperties.key), "AES")
	private val iv = IvParameterSpec(Base64Utils.decodeFromString(jwtProperties.iv))

	fun issue(payload: UserAuth): String {
		val expire = Date(System.currentTimeMillis() + jwtProperties.duration.toMillis())

		return Jwts.builder()
			.setSubject(payload.idNo)
			.setExpiration(expire)
			.claim(CLAIM_NAME, encryptPayload(payload))
			.signWith(jwtKey)
			.compact()
	}

	fun verify(jwt: String): UserAuth {
		return Jwts.parserBuilder()
			.setSigningKey(jwtKey)
			.build()
			.parseClaimsJws(jwt)
			.body
			.get(CLAIM_NAME, String::class.java)
			.run { decryptPayload(this) }
	}

	// https://www.baeldung.com/java-aes-encryption-decryption
	// https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
	fun encryptPayload(payload: UserAuth): String {
		val json = mapper.writeValueAsString(payload)

		return Cipher.getInstance(ALGORITHM)
			.also { it.init(Cipher.ENCRYPT_MODE, aesKey, iv) }
			.run { doFinal(json.toByteArray(StandardCharsets.UTF_8)) }
			.run { Base64Utils.encodeToString(this) }
	}

	fun decryptPayload(encrypted: String): UserAuth {
		val json = Cipher.getInstance(ALGORITHM)
			.also { it.init(Cipher.DECRYPT_MODE, aesKey, iv) }
			.run { doFinal(Base64Utils.decodeFromString(encrypted)) }
			.run { String(this, StandardCharsets.UTF_8) }

		return mapper.readValue(json)
	}
}
