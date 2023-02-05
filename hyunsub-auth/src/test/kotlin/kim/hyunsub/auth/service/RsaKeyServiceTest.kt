package kim.hyunsub.auth.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.Base64Utils

@SpringBootTest
class RsaKeyServiceTest(
	val service: RsaKeyService,
) : FreeSpec({
//	"encrypt public - decrypt private" {
//		val text = "Hello, World!"
//		val cipherText = service.encrypt(text)
//		println(cipherText)
//		val plainText = service.decrypt(cipherText)
//		plainText shouldBe text
//
//		println("private")
//		println(Base64Utils.encodeToString(service.keyPair.private.encoded))
//
//		println("public")
//		println(Base64Utils.encodeToString(service.keyPair.public.encoded))
//	}
//
//	"crypt for token" {
//		val text = "Hello, World!"
//		val cipherText = service.encryptForToken(text)
//		println(cipherText)
//		val plainText = service.decryptForToken(cipherText)
//		plainText shouldBe text
//	}
})
