package kim.hyunsub.auth.controller.auth

import kim.hyunsub.auth.model.RsaKeyResult
import kim.hyunsub.auth.service.RsaKeyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth/rsa-key")
class RsaKeyController(private val rsaKeyService: RsaKeyService) {
	@GetMapping("")
	fun getRsaKey(): RsaKeyResult {
		return RsaKeyResult(rsaKeyService.getPublicKeyBase64())
	}
}
