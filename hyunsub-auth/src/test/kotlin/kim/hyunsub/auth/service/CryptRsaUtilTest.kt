package kim.hyunsub.auth.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.Base64

class CryptRsaUtilTest : FreeSpec({
	val keyPair = CryptRsaUtil.generateKeyPair()
	val plaintext = "Hello, World!"

	data class Data(
		val string: String,
		val stringList: List<String>,
	)

	"crypt with key" {
		val ciphertext1 = CryptRsaUtil.encrypt(plaintext, keyPair.public)
		val decrypted1 = CryptRsaUtil.decrypt(ciphertext1, keyPair.private)
		decrypted1 shouldBe plaintext

		val ciphertext2 = CryptRsaUtil.encrypt(plaintext, keyPair.public)
		val decrypted2 = CryptRsaUtil.decrypt(ciphertext2, keyPair.private)
		decrypted2 shouldBe plaintext

		ciphertext1 shouldNotBe ciphertext2
	}

	"crypt with base64 key" {
		val privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.private.encoded)
		val publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.public.encoded)
		val privateKey = CryptRsaUtil.retrievePrivateKey(privateKeyBase64)
		val publicKey = CryptRsaUtil.retrievePublicKey(publicKeyBase64)

		val ciphertext = CryptRsaUtil.encrypt(plaintext, publicKey)
		val decrypted = CryptRsaUtil.decrypt(ciphertext, privateKey)
		decrypted shouldBe plaintext
	}

	"crypt object" {
		val original = Data(
			string = "kotest_string",
			stringList = listOf("kotest_string_list_item_1")
		)

		val encrypted = CryptRsaUtil.encryptObj(original, keyPair.private)
		encrypted.string shouldNotBe original.string
		encrypted.stringList shouldNotBe original.stringList
		encrypted.stringList[0] shouldNotBe original.stringList[0]

		val decrypted = CryptRsaUtil.decryptObj(encrypted, keyPair.public)
		decrypted shouldBe original
	}

	"sign, verify" {
		val signature = CryptRsaUtil.sign(plaintext, keyPair.private)
		println("signature: $signature")
		CryptRsaUtil.verify(plaintext, signature, keyPair.public) shouldBe true
	}
})
