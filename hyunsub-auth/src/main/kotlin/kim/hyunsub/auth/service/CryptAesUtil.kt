package kim.hyunsub.auth.service

import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptAesUtil {
	private const val ALGORITHM = "AES"
	private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

	fun generateKey(): SecretKey =
		KeyGenerator.getInstance(ALGORITHM)
			.also { it.init(256) }
			.generateKey()

	fun generateIv(): IvParameterSpec =
		ByteArray(16)
			.also { SecureRandom().nextBytes(it) }
			.let { IvParameterSpec(it) }

	fun retrieveKey(keyBase64: String): SecretKeySpec {
		return SecretKeySpec(Base64.getDecoder().decode(keyBase64), ALGORITHM)
	}

	fun retrieveIv(ivBase64: String): IvParameterSpec {
		return IvParameterSpec(Base64.getDecoder().decode(ivBase64))
	}

	fun encrypt(plaintext: String, key: Key, iv: IvParameterSpec? = null): String =
		Cipher.getInstance(TRANSFORMATION)
			.also { it.init(Cipher.ENCRYPT_MODE, key, iv) }
			.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
			.run { Base64.getEncoder().encodeToString(this) }

	fun decrypt(ciphertext: String, key: Key, iv: IvParameterSpec? = null): String =
		Cipher.getInstance(TRANSFORMATION)
			.also { it.init(Cipher.DECRYPT_MODE, key, iv) }
			.doFinal(Base64.getDecoder().decode(ciphertext))
			.let { String(it, StandardCharsets.UTF_8) }
}
