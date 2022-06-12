package kim.hyunsub.auth.service

import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.nio.charset.StandardCharsets
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher

@Service
class RsaKeyService {
	private val keyPair: KeyPair =
		KeyPairGenerator.getInstance("RSA")
			.apply { initialize(2048) }
			.generateKeyPair()

	fun getPublicKeyBase64(): String {
		return Base64Utils.encodeToString(keyPair.public.encoded)
	}

	fun decrypt(ciphertext: String): String {
		return Cipher.getInstance("RSA")
			.apply { init(Cipher.DECRYPT_MODE, keyPair.private) }
			.doFinal(Base64Utils.decodeFromString(ciphertext))
			.let { String(it, StandardCharsets.UTF_8) }
	}

	fun encrypt(plaintext: String): String {
		return Cipher.getInstance("RSA")
			.apply { init(Cipher.ENCRYPT_MODE, keyPair.public) }
			.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
			.let { Base64Utils.encodeToString(it) }
	}
}
