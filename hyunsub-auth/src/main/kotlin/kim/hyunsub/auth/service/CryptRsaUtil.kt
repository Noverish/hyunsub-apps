package kim.hyunsub.auth.service

import org.springframework.util.Base64Utils
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object CryptRsaUtil {
	private const val ALGORITHM = "RSA"

	fun generateKeyPair(): KeyPair =
		KeyPairGenerator.getInstance(ALGORITHM)
			.apply { initialize(2048) }
			.generateKeyPair()

	fun encrypt(plaintext: String, key: Key): String =
		Cipher.getInstance(ALGORITHM)
			.apply { init(Cipher.ENCRYPT_MODE, key) }
			.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
			.let { Base64Utils.encodeToString(it) }

	fun decrypt(ciphertext: String, key: Key): String =
		Cipher.getInstance(ALGORITHM)
			.apply { init(Cipher.DECRYPT_MODE, key) }
			.doFinal(Base64Utils.decodeFromString(ciphertext))
			.let { String(it, StandardCharsets.UTF_8) }

	fun retrievePrivateKey(privateKeyBase64: String): PrivateKey {
		val bytes = Base64Utils.decodeFromString(privateKeyBase64)
		return KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(bytes))
	}

	fun retrievePublicKey(privateKeyBase64: String): PublicKey {
		val bytes = Base64Utils.decodeFromString(privateKeyBase64)
		return KeyFactory.getInstance(ALGORITHM).generatePublic(X509EncodedKeySpec(bytes))
	}

	inline fun <reified T : Any> encryptObj(obj: T, key: Key) =
		encryptObj(obj, key, true, T::class)

	inline fun <reified T : Any> decryptObj(obj: T, key: Key) =
		encryptObj(obj, key, false, T::class)

	fun <T : Any> encryptObj(obj: T, key: Key, isEncrypt: Boolean, type: KClass<T>): T {
		if (!type.isData) {
			throw IllegalArgumentException("Only data class can be crypt")
		}

		val primaryConstructor = obj::class.primaryConstructor!!
		val propertyMap = type.declaredMemberProperties.associateBy { it.name }

		val parameters = primaryConstructor.parameters.map {
			val property = propertyMap[it.name]!!
			when (val value = property.invoke(obj)) {
				is String -> if (isEncrypt) encrypt(value, key) else decrypt(value, key)
				is List<*> -> cryptList(value, key, isEncrypt)
				else -> value
			}
		}

		return primaryConstructor.call(*parameters.toTypedArray())
	}

	private fun cryptList(list: List<*>, key: Key, isEncrypt: Boolean): List<*> =
		list.map {
			when (it) {
				is String -> if (isEncrypt) encrypt(it, key) else decrypt(it, key)
				else -> it
			}
		}

	fun sign(text: String, key: PrivateKey): String =
		Signature.getInstance("SHA256withRSA")
			.apply { initSign(key) }
			.apply { update(text.toByteArray()) }
			.sign()
			.let { Base64Utils.encodeToString(it) }

	fun verify(text: String, signature: String, key: PublicKey): Boolean =
		Signature.getInstance("SHA256withRSA")
			.apply { initVerify(key) }
			.apply { update(text.toByteArray()) }
			.verify(Base64Utils.decodeFromString(signature))
}
