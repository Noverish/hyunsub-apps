package kim.hyunsub.common.kms

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class AesExample : FreeSpec({
	val algorithm = "AES/CBC/PKCS5Padding"
	val plaintext = "Hello, World!"

	"random key and iv" {
		val iv = ByteArray(16)
			.apply { SecureRandom().nextBytes(this) }
			.run {
				println("iv: ${Base64.getEncoder().encodeToString(this)}")
				IvParameterSpec(this)
			}

		val key: SecretKey = KeyGenerator.getInstance("AES")
			.apply { init(256) }
			.generateKey()
		println("key: ${Base64.getEncoder().encodeToString(key.encoded)}")

		val ciphertext = Cipher.getInstance(algorithm)
			.apply { init(Cipher.ENCRYPT_MODE, key, iv) }
			.run { doFinal(plaintext.toByteArray(StandardCharsets.UTF_8)) }
			.run { Base64.getEncoder().encodeToString(this) }
		println("ciphertext: $ciphertext")

		val result = Cipher.getInstance(algorithm)
			.apply { init(Cipher.DECRYPT_MODE, key, iv) }
			.run { doFinal(Base64.getDecoder().decode(ciphertext)) }
			.run { String(this, StandardCharsets.UTF_8) }

		result shouldBe plaintext
	}
})
