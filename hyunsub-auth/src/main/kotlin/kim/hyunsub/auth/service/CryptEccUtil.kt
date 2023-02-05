package kim.hyunsub.auth.service

import org.springframework.util.Base64Utils
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object CryptEccUtil {
	private const val ALGORITHM = "EC"
	private const val DOMAIN = "secp521r1"

	fun generateKeyPair(): KeyPair =
		KeyPairGenerator.getInstance(ALGORITHM)
			.apply { initialize(ECGenParameterSpec(DOMAIN)) }
			.generateKeyPair()

	fun retrievePrivateKey(privateKeyBase64: String): PrivateKey {
		val bytes = Base64Utils.decodeFromString(privateKeyBase64)
		return KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(bytes))
	}

	fun retrievePublicKey(publicKeyBase64: String): PublicKey {
		val bytes = Base64Utils.decodeFromString(publicKeyBase64)
		return KeyFactory.getInstance(ALGORITHM).generatePublic(X509EncodedKeySpec(bytes))
	}
}
