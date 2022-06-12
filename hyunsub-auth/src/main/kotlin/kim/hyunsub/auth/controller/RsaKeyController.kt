package kim.hyunsub.auth.controller

import kim.hyunsub.auth.model.RsaKeyResult
import kim.hyunsub.auth.service.RsaKeyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import java.security.KeyPair
import java.security.KeyPairGenerator

@RestController
@RequestMapping("/api/v1/rsa-key")
class RsaKeyController(private val rsaKeyService: RsaKeyService) {
	@GetMapping("")
	fun getRsaKey(): RsaKeyResult {
		return RsaKeyResult(rsaKeyService.getPublicKeyBase64())
	}
}
