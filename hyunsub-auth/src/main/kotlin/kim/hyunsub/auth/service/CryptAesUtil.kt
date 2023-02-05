package kim.hyunsub.auth.service

import org.springframework.util.Base64Utils
import java.nio.charset.StandardCharsets
import java.security.*
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
		return SecretKeySpec(Base64Utils.decodeFromString(keyBase64), ALGORITHM)
	}

	fun retrieveIv(ivBase64: String): IvParameterSpec {
		return IvParameterSpec(Base64Utils.decodeFromString(ivBase64))
	}

	fun encrypt(plaintext: String, key: Key, iv: IvParameterSpec? = null): String =
		Cipher.getInstance(TRANSFORMATION)
			.also { it.init(Cipher.ENCRYPT_MODE, key, iv) }
			.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
			.run { Base64Utils.encodeToString(this) }

	fun decrypt(ciphertext: String, key: Key, iv: IvParameterSpec? = null): String =
		Cipher.getInstance(TRANSFORMATION)
			.also { it.init(Cipher.DECRYPT_MODE, key, iv) }
			.doFinal(Base64Utils.decodeFromString(ciphertext))
			.let { String(it, StandardCharsets.UTF_8) }
}
