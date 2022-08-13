package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.kms.KmsDecryptApiParams
import kim.hyunsub.auth.model.kms.KmsEncryptApiParams
import kim.hyunsub.common.kms.KmsEncryptor
import kim.hyunsub.common.kms.KmsProperties
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.originalIp
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/kms")
class KmsController(private val kmsProperties: KmsProperties) {
	companion object : Log

	@PostMapping("/encrypt")
	fun encrypt(request: HttpServletRequest, @RequestBody params: KmsEncryptApiParams): String {
		val text = params.text
		log.info("KMS Encrypt: {} - {}", request.originalIp, text)
		return KmsEncryptor.encrypt(kmsProperties.profile, kmsProperties.keyId, text)
	}

	@PostMapping("/decrypt")
	fun decrypt(request: HttpServletRequest, @RequestBody params: KmsDecryptApiParams): String {
		val cipher = params.cipher
		log.info("KMS Decrypt: {} - {}", request.originalIp, cipher)
		return KmsEncryptor.decrypt(kmsProperties.profile, kmsProperties.keyId, cipher)
	}
}
