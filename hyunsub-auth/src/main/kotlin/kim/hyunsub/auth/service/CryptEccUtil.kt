package kim.hyunsub.auth.service

import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object CryptEccUtil {
	private const val ALGORITHM = "EC"
	private const val DOMAIN = "secp521r1"

	fun generateKeyPair(): KeyPair =
		KeyPairGenerator.getInstance(ALGORITHM)
			.apply { initialize(ECGenParameterSpec(DOMAIN)) }
			.generateKeyPair()

	fun retrievePrivateKey(privateKeyBase64: String): PrivateKey {
		val bytes = Base64.getDecoder().decode(privateKeyBase64)
		return KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(bytes))
	}

	fun retrievePublicKey(publicKeyBase64: String): PublicKey {
		val bytes = Base64.getDecoder().decode(publicKeyBase64)
		return KeyFactory.getInstance(ALGORITHM).generatePublic(X509EncodedKeySpec(bytes))
	}
}
