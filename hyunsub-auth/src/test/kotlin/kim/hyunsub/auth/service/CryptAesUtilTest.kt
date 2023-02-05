package kim.hyunsub.auth.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.util.Base64Utils

class CryptAesUtilTest : FreeSpec({
	val key = CryptAesUtil.generateKey()
	val iv = CryptAesUtil.generateIv()
	val plaintext = "Hello, World!"

	"key" {
		val ciphertext1 = CryptAesUtil.encrypt(plaintext, key, iv)
		val decrypted1 = CryptAesUtil.decrypt(ciphertext1, key, iv)
		decrypted1 shouldBe plaintext

		val ciphertext2 = CryptAesUtil.encrypt(plaintext, key, iv)
		val decrypted2 = CryptAesUtil.decrypt(ciphertext2, key, iv)
		decrypted2 shouldBe plaintext

		ciphertext1 shouldBe ciphertext2
	}

	"base64" {
		val keyBase64 = Base64Utils.encodeToString(key.encoded)
		val ivBase64 = Base64Utils.encodeToString(iv.iv)
		val retrievedKey = CryptAesUtil.retrieveKey(keyBase64)
		val retrievedIv = CryptAesUtil.retrieveIv(ivBase64)

		val ciphertext1 = CryptAesUtil.encrypt(plaintext, retrievedKey, retrievedIv)
		val decrypted1 = CryptAesUtil.decrypt(ciphertext1, retrievedKey, retrievedIv)
		decrypted1 shouldBe plaintext

		val ciphertext2 = CryptAesUtil.encrypt(plaintext, retrievedKey, retrievedIv)
		val decrypted2 = CryptAesUtil.decrypt(ciphertext2, retrievedKey, retrievedIv)
		decrypted2 shouldBe plaintext

		ciphertext1 shouldBe ciphertext2
	}
})
