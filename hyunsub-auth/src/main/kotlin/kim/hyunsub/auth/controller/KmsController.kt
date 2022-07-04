package kim.hyunsub.auth.controller

import kim.hyunsub.common.kms.KmsEncryptor
import kim.hyunsub.common.kms.KmsProperties
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.isLocalhost
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/admin/kms")
class KmsController(private val kmsProperties: KmsProperties) {
	companion object : Log

	@PostMapping("/encrypt")
	fun encrypt(request: HttpServletRequest, @RequestParam text: String): String {
		log.info("KMS Encrypt: {} - {}", request.remoteAddr, text)
		return if (request.isLocalhost()) {
			KmsEncryptor.encrypt(kmsProperties.profile, kmsProperties.keyId, text)
		} else {
			request.remoteAddr
		}
	}

	@PostMapping("/decrypt")
	fun decrypt(request: HttpServletRequest, @RequestParam cipher: String): String {
		log.info("KMS Decrypt: {} - {}", request.remoteAddr, cipher)
		return if (request.isLocalhost()) {
			KmsEncryptor.decrypt(kmsProperties.profile, kmsProperties.keyId, cipher)
		} else {
			request.remoteAddr
		}
	}
}
