package kim.hyunsub.auth.service

import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.security.KeyPair
import java.security.KeyPairGenerator

@Service
class RsaKeyService {
	val keyPair: KeyPair = KeyPairGenerator.getInstance("RSA")
		.apply { initialize(2048) }
		.generateKeyPair()

	fun getPublicKeyBase64(): String =
		Base64Utils.encodeToString(keyPair.public.encoded)

	fun decrypt(ciphertext: String): String =
		CryptRsaUtil.decrypt(ciphertext, keyPair.private)

	fun encrypt(plaintext: String): String =
		CryptRsaUtil.encrypt(plaintext, keyPair.public)
}
