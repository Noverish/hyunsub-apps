package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.dto.KmsDecryptParams
import kim.hyunsub.auth.model.dto.KmsEncryptParams
import kim.hyunsub.common.kms.KmsEncryptor
import kim.hyunsub.common.kms.KmsProperties
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/kms")
class KmsController(private val kmsProperties: KmsProperties) {
	companion object : Log

	@PostMapping("/encrypt")
	fun encrypt(@RequestBody params: KmsEncryptParams): String {
		val text = params.text
		log.info("KMS Encrypt: {}", text)
		return KmsEncryptor.encrypt(kmsProperties.profile, kmsProperties.keyId, text)
	}

	@PostMapping("/decrypt")
	fun decrypt(@RequestBody params: KmsDecryptParams): String {
		val cipher = params.cipher
		log.info("KMS Decrypt: {}", cipher)
		return KmsEncryptor.decrypt(kmsProperties.profile, kmsProperties.keyId, cipher)
	}
}
