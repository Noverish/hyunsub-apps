package kim.hyunsub.auth.controller.auth

import kim.hyunsub.auth.model.auth.ValidUrlParams
import kim.hyunsub.auth.model.auth.ValidUrlResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URL

@RestController
@RequestMapping("/api/v1/auth/valid-url")
class ValidUrlController {
	@PostMapping("")
	fun validUrl(@RequestBody params: ValidUrlParams): ValidUrlResult {
		val url = params.url
		if (url.startsWith("/") || url == "") {
			return ValidUrlResult(true)
		}

		val urlObj = URL(url)
		val valid = urlObj.host.endsWith(".hyunsub.kim") && urlObj.protocol == "https"
		return ValidUrlResult(valid)
	}
}
